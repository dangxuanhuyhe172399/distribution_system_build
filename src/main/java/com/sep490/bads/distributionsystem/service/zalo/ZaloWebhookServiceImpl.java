package com.sep490.bads.distributionsystem.service.zalo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sep490.bads.distributionsystem.config.zalo.ZaloApiClient;
import com.sep490.bads.distributionsystem.entity.Customer;
import com.sep490.bads.distributionsystem.entity.CustomerType;
import com.sep490.bads.distributionsystem.entity.SalesOrder;
import com.sep490.bads.distributionsystem.entity.type.CustomerStatus;
import com.sep490.bads.distributionsystem.entity.type.ZaloEventLogStatus;
import com.sep490.bads.distributionsystem.entity.zalo.ZaloCustomerLink;
import com.sep490.bads.distributionsystem.entity.zalo.ZaloEventLog;
import com.sep490.bads.distributionsystem.repository.CustomerRepository;
import com.sep490.bads.distributionsystem.repository.CustomerTypeRepository;
import com.sep490.bads.distributionsystem.repository.zalo.ZaloCustomerLinkRepository;
import com.sep490.bads.distributionsystem.repository.zalo.ZaloEventLogRepository;
import com.sep490.bads.distributionsystem.service.InventoryService;
import com.sep490.bads.distributionsystem.service.SalesOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ZaloWebhookServiceImpl implements ZaloWebhookService {
    private final ZaloEventLogRepository eventLogRepo;
    private final ZaloCustomerLinkRepository zaloCustomerLinkRepo;
    private final CustomerRepository customerRepo;
    private final CustomerTypeRepository customerTypeRepo;
    private final SalesOrderService salesOrderService;
    private final InventoryService inventoryService;
    private final ZaloApiClient zaloApiClient;

    private final ObjectMapper om = new ObjectMapper();

    /**
     * Tên loại KH mặc định khi tạo mới từ Zalo.
     * Hãy đảm bảo tồn tại trong DB.
     */
    private static final String DEFAULT_CUSTOMER_TYPE_NAME = "Khách lẻ";

    @Override
    @Async("zaloExecutor")
    @Transactional
    public void handle(String body) {
        ZaloEventLog logRow = new ZaloEventLog();
        try {
            JsonNode root     = om.readTree(body);
            String eventName  = txt(root,"event_name");
            String eventId    = txt(root,"event_id");

            // Idempotent theo event_id (nếu có)
            if (eventId != null && !eventId.isBlank()) {
                if (eventLogRepo.findByEventId(eventId).isPresent()) {
                    log.info("Skip duplicate event {}", eventId);
                    return;
                }
            }

            logRow.setEventId(isBlank(eventId) ? null : eventId);
            logRow.setEventName(eventName);
            logRow.setPayload(body);
            logRow.setStatus(ZaloEventLogStatus.RECEIVED);
            logRow.setReceivedAt(LocalDateTime.now());
            eventLogRepo.save(logRow);

            switch (eventName) {
                case "follow"            -> onFollow(root);
                case "unfollow"          -> onUnfollow(root);
                case "user_share_info",
                     "user_submit_info"  -> onUserShareInfo(root);
                case "user_send_text",
                     "oa_send_text"      -> onTextMessage(root);
                case "order_created"     -> onOrderCreated(root);
                case "order_updated"     -> onOrderUpdated(root);
                default -> log.info("Unhandled event {}", eventName);
            }

            logRow.setStatus(ZaloEventLogStatus.PROCESSED);
            logRow.setProcessedAt(LocalDateTime.now());
            eventLogRepo.save(logRow);
        } catch (Exception ex) {
            log.error("Handle webhook error", ex);
            logRow.setStatus(ZaloEventLogStatus.FAILED);
            logRow.setProcessedAt(LocalDateTime.now());
            if (logRow.getReceivedAt() == null) logRow.setReceivedAt(LocalDateTime.now());
            logRow.setPayload(body);
            eventLogRepo.save(logRow);
        }
    }

    /* ========== Handlers ========== */

    private void onTextMessage(JsonNode root) {
        String uid = txt(root.path("sender"), "id");
        if (isBlank(uid)) uid = txt(root.path("from"), "id");
        if (isBlank(uid)) return;

        String text = txt(root.path("message"), "text");

        // cập nhật link tối thiểu
        String finalUid = uid;
        ZaloCustomerLink  link = zaloCustomerLinkRepo.findById(uid).orElseGet(() -> {
            var l = new ZaloCustomerLink();
            l.setZaloUserId(finalUid);
            l.setFollowStatus("unknown");
            return l;
        });
//         link.setLastMessageAt(LocalDateTime.now());
        // link.setLastMessageText(text);
        zaloCustomerLinkRepo.save(link);

        // Nếu chưa có customer → gợi ý chia sẻ info
        // if (link.getCustomer() == null) sendAskForShareInfo(uid);
        log.info("Received text '{}' from {}", text, uid);
    }

    private void onFollow(JsonNode root) {
        String zaloUserId = txt(root.path("follower"), "id");
        if (isBlank(zaloUserId)) zaloUserId = txt(root.path("sender"), "id");
        if (isBlank(zaloUserId)) return;

        String finalZaloUserId = zaloUserId;
        ZaloCustomerLink  link = zaloCustomerLinkRepo.findById(zaloUserId).orElseGet(() -> {
            ZaloCustomerLink l = new ZaloCustomerLink();
            l.setZaloUserId(finalZaloUserId);
            l.setCreatedAt(LocalDateTime.now());
            return l;
        });
        link.setFollowStatus("follow");
        zaloCustomerLinkRepo.save(link);
        log.info("User {} followed OA", zaloUserId);
    }

    private void onUnfollow(JsonNode root) {
        String zaloUserId = txt(root.path("follower"), "id");
        if (isBlank(zaloUserId)) zaloUserId = txt(root.path("sender"), "id");
        if (isBlank(zaloUserId)) return;

        zaloCustomerLinkRepo.findById(zaloUserId).ifPresent(link -> {
            link.setFollowStatus("unfollow");
            zaloCustomerLinkRepo.save(link);
        });
        log.info("User {} unfollowed OA", zaloUserId);
    }

    /**
     * Người dùng bấm “Chia sẻ thông tin”.
     * Lưu ý: payload Zalo có thể khác nhau (v2 vs v3). Dưới đây mình đọc linh hoạt:
     * - user id: sender.id || follower.id
     * - info   : info.{name,phone,address,email} || user_info.{...}
     */
    private void onUserShareInfo(JsonNode root) {
            String zaloUserId = firstNotBlank(
                    txt(root.path("sender"), "id"),
                    txt(root.path("follower"), "id")
            );
            if (isBlank(zaloUserId)) {
                log.warn("user_share_info but missing zaloUserId");
                return;
            }

            // Lấy node info: tùy version Zalo trả "info" hoặc "user_info"
            JsonNode info = root.path("info").isMissingNode()
                    ? root.path("user_info")
                    : root.path("info");

            String rawPhone  = firstNotBlank(
                    txt(info, "phone"),
                    txt(info, "mobile"),
                    txt(info, "msisdn")
            );
            String name      = firstNotBlank(
                    txt(info, "name"),
                    txt(info, "full_name"),
                    txt(info, "username")
            );
            String address   = txt(info, "address");
            String email     = txt(info, "email");
            String district  = txt(info, "district");
            String province  = txt(info, "province");

            if (isBlank(rawPhone) && isBlank(email)) {
                log.warn("user_share_info missing both phone & email for {}", zaloUserId);
                return;
            }

            String phone = normalizePhoneVN(rawPhone);

            log.info("user_share_info from {} -> phone={}, name={}, email={}",
                    zaloUserId, phone, name, email);

            // 0) Thử lấy link theo zaloUserId trước
            ZaloCustomerLink link = zaloCustomerLinkRepo.findById(zaloUserId).orElse(null);
            Customer customer = (link != null ? link.getCustomer() : null);

            // 1) Nếu chưa có customer gắn với link, thử tìm theo phone/email
            if (customer == null && !isBlank(phone)) {
                customer = customerRepo.findFirstByPhone(phone).orElse(null);
            }
            if (customer == null && !isBlank(email)) {
                customer = customerRepo.findFirstByEmail(email).orElse(null);
            }

            // 2) Nếu vẫn null => tạo mới
            if (customer == null) {
                CustomerType type = customerTypeRepo.findByName(DEFAULT_CUSTOMER_TYPE_NAME)
                        .orElseThrow(() -> new IllegalStateException(
                                "Missing CustomerType '" + DEFAULT_CUSTOMER_TYPE_NAME + "'"));

                customer = new Customer();
                customer.setCode("ZLO-" + UUID.randomUUID().toString().substring(0, 8));
                customer.setType(type);
                customer.setStatus(CustomerStatus.ACTIVE);
                customer.setCreatedAt(LocalDateTime.now());
            }

            // 3) Luôn update các field nếu Zalo gửi về (không cần chờ blank)
            if (!isBlank(name))     customer.setName(name);
            if (!isBlank(phone))    customer.setPhone(phone);
            if (!isBlank(email))    customer.setEmail(email);
            if (!isBlank(address))  customer.setAddress(address);
            if (!isBlank(district)) customer.setDistrict(district);
            if (!isBlank(province)) customer.setProvince(province);

            customer = customerRepo.save(customer);

            // 4) Cập nhật / tạo link
            if (link == null) {
                link = new ZaloCustomerLink();
                link.setZaloUserId(zaloUserId);
                link.setCreatedAt(LocalDateTime.now());
            }
            link.setCustomer(customer);
            link.setFollowStatus(nonBlankOr(link.getFollowStatus(), "follow"));
            link.setConsentAt(LocalDateTime.now());
            zaloCustomerLinkRepo.save(link);

            log.info("Linked Zalo {} → Customer {} ({}, {})",
                    zaloUserId, customer.getId(), customer.getName(), customer.getPhone());

            // 5) Gửi cảm ơn (optional)
            try {
                zaloApiClient.sendText(zaloUserId,
                                "Cảm ơn bạn đã chia sẻ thông tin! Hệ thống đã lưu thông tin liên hệ của bạn.")
                        .subscribe();
            } catch (Exception e) {
                log.warn("Send thank-you message failed but data saved: {}", e.getMessage());
            }
        }



        /* Hai sự kiện này để dành cho tích hợp shop của Zalo OA (nếu dùng) */
    private void onOrderCreated(JsonNode root) {
        log.info("Zalo order_created: {}", root);
        // TODO: ánh xạ và tạo SalesOrder nếu bạn dùng Zalo Shop
    }

    private void onOrderUpdated(JsonNode root) {
        log.info("Zalo order_updated: {}", root);
        // TODO: cập nhật trạng thái đơn/Invoice khi có thay đổi
    }

    /* ========== Helpers ========== */

    private static String txt(JsonNode node, String field) {
        JsonNode v = node.path(field);
        return v.isMissingNode() || v.isNull() ? null : v.asText(null);
    }
    private static boolean isBlank(String s){ return s==null || s.isBlank(); }
    private static String nonBlankOr(String s, String alt){ return isBlank(s) ? alt : s; }
    private static String firstNotBlank(String... arr){
        for (String s: arr) if (!isBlank(s)) return s;
        return null;
    }
    private static String normalizePhoneVN(String p){
        if (p == null) return null;
        String s = p.replaceAll("[^0-9+]", "");
        if (s.startsWith("+84")) return "0" + s.substring(3);
        if (s.startsWith("84") && s.length() >= 10) return "0" + s.substring(2);
        return s;
    }
}
