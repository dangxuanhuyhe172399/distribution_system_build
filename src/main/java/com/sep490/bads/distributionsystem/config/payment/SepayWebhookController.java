package com.sep490.bads.distributionsystem.config.payment;

import com.sep490.bads.distributionsystem.controller.BaseController;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/v1/sepay/hooks")
public class SepayWebhookController extends BaseController {
    private final String apiKey;
    public SepayWebhookController(@Value("${sepay.webhook.apiKey:}") String apiKey) {
        this.apiKey = apiKey == null ? "" : apiKey.trim();
    }

    @PostConstruct
    void init() {
        log.info("SePay apiKey length={}, mask={}...{}",
                apiKey.length(),
                apiKey.substring(0, Math.min(4, apiKey.length())),
                apiKey.substring(Math.max(apiKey.length()-4, 0)));
    }

    @PostMapping(value="/sepay-payment", consumes="application/json", produces="application/json")
    public ResponseEntity<?> handle(
            @RequestHeader(value="Authorization", required=false) String auth,
            @RequestHeader(value="X-Secret-Key", required=false) String xSecret,
            @RequestBody Map<String,Object> payload) {

        String provided = "";
        if (auth != null) {
            provided = auth.regionMatches(true, 0, "Apikey ", 0, 7) ? auth.substring(7) : auth;
            provided = provided.trim();
        }

        log.info("authPresent={}, xSecretPresent={}, providedLen={}, xSecretLen={}, providedMask={}...{}, xSecretMask={}...{}",
                auth != null, xSecret != null,
                provided.length(), xSecret == null ? 0 : xSecret.length(),
                provided.isEmpty() ? "" : provided.substring(0, Math.min(4, provided.length())),
                provided.isEmpty() ? "" : provided.substring(Math.max(provided.length()-4, 0)),
                xSecret == null || xSecret.isEmpty() ? "" : xSecret.substring(0, Math.min(4, xSecret.length())),
                xSecret == null || xSecret.isEmpty() ? "" : xSecret.substring(Math.max(xSecret.length()-4, 0))
        );

        boolean ok = !apiKey.isEmpty() &&
                (apiKey.equals(provided) || (xSecret != null && apiKey.equals(xSecret.trim())));

        if (!ok) return ResponseEntity.status(401).body(Map.of("code", 401, "message", "Invalid token"));
        return ResponseEntity.status(201).body(Map.of("success", true));
    }
}

