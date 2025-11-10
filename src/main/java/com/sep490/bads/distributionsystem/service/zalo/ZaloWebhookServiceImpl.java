package com.sep490.bads.distributionsystem.service.zalo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sep490.bads.distributionsystem.entity.Customer;
import com.sep490.bads.distributionsystem.entity.CustomerType;
import com.sep490.bads.distributionsystem.entity.type.CustomerStatus;
import com.sep490.bads.distributionsystem.entity.type.ZaloEventLogStatus;
import com.sep490.bads.distributionsystem.entity.zalo.ZaloCustomerLink;
import com.sep490.bads.distributionsystem.entity.zalo.ZaloEventLog;
import com.sep490.bads.distributionsystem.repository.CustomerRepository;
import com.sep490.bads.distributionsystem.repository.zalo.ZaloCustomerLinkRepository;
import com.sep490.bads.distributionsystem.repository.zalo.ZaloEventLogRepository;
import com.sep490.bads.distributionsystem.service.InventoryService;
import com.sep490.bads.distributionsystem.service.SalesOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ZaloWebhookServiceImpl implements ZaloWebhookService {
    private final ZaloEventLogRepository eventLogRepo;
    private final ZaloCustomerLinkRepository zaloCustomerLinkRepo;
    private final CustomerRepository customerRepo;
    private final ObjectMapper om = new ObjectMapper();
    private final SalesOrderService salesOrderService;
    private final InventoryService inventoryService;

    /**
     * Tên của Loại khách hàng mặc định khi tạo mới từ Zalo.
     * BẠN PHẢI TẠO CustomerType VỚI TÊN NÀY TRONG DATABASE TRƯỚC.
     */
    private static final String DEFAULT_CUSTOMER_TYPE_NAME = "Khách lẻ";

    @Override
    @Transactional
    public void handle(String body) {
        ZaloEventLog logRow = new ZaloEventLog();
        try {
            JsonNode root = om.readTree(body);
            String eventName = root.path("event_name").asText("");
            String eventId   = root.path("event_id").asText("");

            var existed = (eventId != null && !eventId.isBlank())
                    ? eventLogRepo.findByEventId(eventId) : Optional.<ZaloEventLog>empty();

            if (existed.isPresent()) {
                ZaloEventLog dup = existed.get();
                dup.setEventName(eventName);
                dup.setPayload(body);
                dup.setStatus(ZaloEventLogStatus.DUPLICATE);
                dup.setProcessedAt(LocalDateTime.now());
                return;
            }

            logRow.setEventId(eventId.isBlank() ? null : eventId);
            logRow.setEventName(eventName);
            logRow.setPayload(body);
            logRow.setStatus(ZaloEventLogStatus.RECEIVED);
            logRow.setReceivedAt(LocalDateTime.now());
            eventLogRepo.save(logRow);

            switch (eventName) {
                case "follow"         -> onFollow(root);
                case "unfollow"       -> onUnfollow(root);
                case "user_share_info",
                     "user_submit_info"  -> onUserShareInfo(root);
                case "order_created"  -> onOrderCreated(root);
                case "order_updated"  -> onOrderUpdated(root);
                default               -> log.info("Unhandled event {}", eventName);
            }

            logRow.setStatus(ZaloEventLogStatus.PROCESSED);
            logRow.setProcessedAt(LocalDateTime.now());

        } catch (Exception ex) {
            ZaloEventLog failed = new ZaloEventLog();
            failed.setEventName("unknown");
            failed.setPayload(body);
            failed.setStatus(ZaloEventLogStatus.FAILED);
            failed.setReceivedAt(LocalDateTime.now());
            failed.setProcessedAt(LocalDateTime.now());
            eventLogRepo.save(failed);
            log.error("Handle webhook error", ex);
        }
    }

    //Xử lý khi người dùng nhấn "Quan tâm" OA.
    private void onFollow(JsonNode root) {
        String zaloUserId = root.path("follower").path("id").asText();
        if (zaloUserId.isBlank()) return;

        // Tìm hoặc Tạo Mới
        ZaloCustomerLink link = zaloCustomerLinkRepo.findById(zaloUserId)
                .orElseGet(ZaloCustomerLink::new);

        link.setZaloUserId(zaloUserId); // Đảm bảo ID được set (cho trường hợp tạo mới)
        link.setFollowStatus("follow"); // Cập nhật trạng thái

        zaloCustomerLinkRepo.save(link);
        log.info("User {} followed OA", zaloUserId);
    }

    //Xử lý khi người dùng nhấn "Bỏ quan tâm" OA.
    private void onUnfollow(JsonNode root) {
        String zaloUserId = root.path("follower").path("id").asText(); // Zalo gửi là 'follower' thay vì 'user'
        if (zaloUserId.isBlank()) return;

        // Chỉ cập nhật nếu đã tồn tại
        zaloCustomerLinkRepo.findById(zaloUserId).ifPresent(link -> {
            link.setFollowStatus("unfollow");
            zaloCustomerLinkRepo.save(link);
            log.info("User {} unfollowed OA", zaloUserId);
        });
    }

    //Xảy ra khi người dùng đồng ý chia sẻ thông tin (Tên, SĐT, Địa chỉ).
    @Transactional
    public void onUserShareInfo(JsonNode root) {
        String zaloUserId = root.path("sender").path("id").asText();
        JsonNode info = root.path("info");

        String phone = info.path("phone").asText(null);
        String name = info.path("name").asText(null);
        String address = info.path("address").asText(null);

        if (zaloUserId.isBlank() || phone == null || phone.isBlank()) {
            log.warn("User {} shared info but phone is missing", zaloUserId);
            return;
        }

        //SĐT
        String standardizedPhone = phone.startsWith("+84") ? "0" + phone.substring(3) : phone;

        // --- BƯỚC 1: TÌM HOẶC TẠO CUSTOMER ---
        Optional<Customer> optCustomer = customerRepo.findByPhone(standardizedPhone);
        Customer customer;

        if (optCustomer.isPresent()) {
            customer = optCustomer.get();
            customer.setName(name);
            customer.setAddress(address);
            log.info("Customer found with phone {}. Updating info.", standardizedPhone);
        } else {
            log.info("Phone {} not found, creating new customer", standardizedPhone);

            // Tìm loại khách hàng mặc định
            CustomerType defaultType = customerRepo.findByName(DEFAULT_CUSTOMER_TYPE_NAME)
                    .orElse(null);

            if (defaultType == null) {
                log.error("CRITICAL: Không thể tạo khách hàng mới. " +
                        "Không tìm thấy CustomerType mặc định với tên = '{}'. " +
                        "Vui lòng tạo trong DB.", DEFAULT_CUSTOMER_TYPE_NAME);
                return;
            }

            customer = new Customer();
            customer.setPhone(standardizedPhone);
            customer.setName(name);
            customer.setAddress(address);
            customer.setStatus(CustomerStatus.ACTIVE);
            customer.setType(defaultType);
        }

        customer = customerRepo.save(customer);

        // --- BƯỚC 2: TÌM HOẶC TẠO ZALOCUSTOMERLINK ---
        ZaloCustomerLink link = zaloCustomerLinkRepo.findById(zaloUserId)
                .orElseGet(ZaloCustomerLink::new);
        link.setZaloUserId(zaloUserId);

        // --- BƯỚC 3: KẾT NỐI 2 BẢNG ---
        link.setCustomer(customer); // Đây là bước "kết nối"
        link.setConsentAt(LocalDateTime.now());

        zaloCustomerLinkRepo.save(link);
        log.info("Successfully linked Zalo User {} to Customer ID {}", zaloUserId, customer.getId()); // Dùng .getId()
    }

    private void onOrderCreated(JsonNode root) {
        // TODO: ánh xạ order -> SalesOrder, tạo nếu chưa có
    }

    private void onOrderUpdated(JsonNode root) {
        // TODO: cập nhật trạng thái đơn, nếu paid -> tạo Invoice
    }
}
