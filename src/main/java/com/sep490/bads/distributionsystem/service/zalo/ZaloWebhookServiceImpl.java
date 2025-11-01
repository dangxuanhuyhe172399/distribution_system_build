package com.sep490.bads.distributionsystem.service.zalo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sep490.bads.distributionsystem.entity.type.ZaloEventLogStatus;
import com.sep490.bads.distributionsystem.entity.zalo.ZaloEventLog;
import com.sep490.bads.distributionsystem.repository.zalo.ZaloEventLogRepository;
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

    @Override
    @Transactional
    public void handle(String body) {
        ZaloEventLog logRow = new ZaloEventLog();
        try {
            JsonNode root = om.readTree(body);
            String eventName = root.path("event_name").asText("");
            String eventId   = root.path("event_id").asText("");

            if (eventId != null && !eventId.isBlank()
                    && eventLogRepo.findByEventId(eventId).isPresent()) {
                logRow.setEventId(eventId);
                logRow.setEventName(eventName);
                logRow.setPayload(body);
                logRow.setStatus(ZaloEventLogStatus.DUPLICATE);
                logRow.setProcessedAt(LocalDateTime.now());
                eventLogRepo.save(logRow);
                return;
            }

            logRow.setEventId(eventId.isBlank() ? null : eventId);
            logRow.setEventName(eventName);
            logRow.setPayload(body);
            logRow.setStatus(ZaloEventLogStatus.RECEIVED);
            eventLogRepo.save(logRow);

            switch (eventName) {
                case "follow"         -> onFollow(root);
                case "unfollow"       -> onUnfollow(root);
                case "user_share_info"-> onUserShareInfo(root);
                case "order_created"  -> onOrderCreated(root);
                case "order_updated"  -> onOrderUpdated(root);
                default               -> log.info("Unhandled event {}", eventName);
            }

            logRow.setStatus(ZaloEventLogStatus.PROCESSED);
            logRow.setProcessedAt(LocalDateTime.now());
            eventLogRepo.save(logRow);

        } catch (Exception ex) {
            logRow.setStatus(ZaloEventLogStatus.FAILED);
            logRow.setProcessedAt(LocalDateTime.now());
            if (logRow.getId() == null) {
                logRow.setPayload(body);
                logRow.setEventName("unknown");
            }
            eventLogRepo.save(logRow);
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
