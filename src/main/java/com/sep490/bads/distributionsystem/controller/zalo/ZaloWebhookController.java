package com.sep490.bads.distributionsystem.controller.zalo;

import com.sep490.bads.distributionsystem.config.zalo.ZaloProperties;
import com.sep490.bads.distributionsystem.controller.BaseController;
import com.sep490.bads.distributionsystem.service.zalo.ZaloWebhookService;
import com.sep490.bads.distributionsystem.utils.HmacUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequestMapping("/v1/public/zalo/webhook")
@RequiredArgsConstructor
public class ZaloWebhookController extends BaseController {

    private final ZaloWebhookService service;
    private final ZaloProperties props;

    @PostMapping
    public ResponseEntity<String> receive(HttpServletRequest request) {
        try {
            String signature = request.getHeader("X-ZEvent-Signature");
            String ts        = request.getHeader("X-ZEvent-Timestamp");
            String body      = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);

            if (props.isVerify() && !verify(signature, ts, body)) {
                log.warn("Invalid signature");
                return ResponseEntity.status(403).body("invalid signature");
            }

            service.handle(body);
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            log.error("Webhook error", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    private boolean verify(String headerSig, String ts, String body) {
        if (headerSig == null || headerSig.isBlank()) return false;
        String mode = props.getSignatureMode();
        String base = switch (mode) {
            case "BODY"    -> body;
            case "BODY_TS", "APP_BODY_TS" -> body + (ts == null ? "" : ts);
            default        -> props.getAppId() + body + (ts == null ? "" : ts);
        };
        String calc = HmacUtils.hmacSha256Hex(props.getOaSecret(), base);
        return headerSig.equalsIgnoreCase(calc);
    }
}
