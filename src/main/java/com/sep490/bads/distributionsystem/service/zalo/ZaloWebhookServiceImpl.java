package com.sep490.bads.distributionsystem.service.zalo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sep490.bads.distributionsystem.entity.type.ZaloEventLogStatus;
import com.sep490.bads.distributionsystem.entity.zalo.ZaloCustomerLink;
import com.sep490.bads.distributionsystem.entity.zalo.ZaloEventLog;
import com.sep490.bads.distributionsystem.repository.zalo.ZaloEventLogRepository;
import com.sep490.bads.distributionsystem.service.InventoryService;
import com.sep490.bads.distributionsystem.service.SalesOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ZaloWebhookServiceImpl implements ZaloWebhookService {
    private final ZaloEventLogRepository eventLogRepo;
    private final ObjectMapper om = new ObjectMapper();
    private final SalesOrderService salesOrderService;
    private final InventoryService inventoryService;

    @Override
    @Transactional
    public void handle(String body) {
        ZaloEventLog logRow = new ZaloEventLog();
        try {
            JsonNode root = om.readTree(body);
            String eventName = root.path("event_name").asText("");
            String eventId   = root.path("event_id").asText("");

            var existed = (eventId != null && !eventId.isBlank())
                    ? eventLogRepo.findByEventId(eventId) : java.util.Optional.<ZaloEventLog>empty();

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

    private void onFollow(JsonNode root) {
    }

    private void onUnfollow(JsonNode root) {
    }

    private void onUserShareInfo(JsonNode root) {
        // TODO: nhận thông tin người dùng đã consent và cập nhật Customer + ZaloCustomerLink.consent_at
    }

    private void onOrderCreated(JsonNode root) {
        // TODO: ánh xạ order -> SalesOrder, tạo nếu chưa có
    }

    private void onOrderUpdated(JsonNode root) {
        // TODO: cập nhật trạng thái đơn, nếu paid -> tạo Invoice
    }
}
