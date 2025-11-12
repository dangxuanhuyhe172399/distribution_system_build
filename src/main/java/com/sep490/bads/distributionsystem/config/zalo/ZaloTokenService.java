package com.sep490.bads.distributionsystem.config.zalo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ZaloTokenService {

    @Qualifier("zaloOauthWebClient")
    private final WebClient webClient;           // baseUrl = https://oauth.zaloapp.com
    private final ZaloProperties props;

    private final ObjectMapper objectMapper;
    // Lưu tạm trong RAM. Prod nên lưu DB/Redis + lock refresh.
    private volatile String accessToken;
    private volatile long   expiresAtEpoch;      // seconds
    private volatile String refreshToken;

    @PostConstruct
    void init() {
        // load từ env cho dễ test
        this.accessToken   = props.getAccessToken();     // zalo.access-token
        this.refreshToken  = props.getRefreshToken();    // zalo.refresh-token (nếu có)
        this.expiresAtEpoch = 0L;                        // ép refresh nếu cần
    }

    public String getAccessToken() {
        long now = System.currentTimeMillis() / 1000;
        if (accessToken == null || now > (expiresAtEpoch - 60)) {
            refresh();
        }
        return accessToken;
    }

    // Refresh theo tài liệu: POST /v4/oa/access_token (form)
    private synchronized void refresh() {
        long now = System.currentTimeMillis() / 1000;
        if (accessToken != null && now < (expiresAtEpoch - 60)) return;

        if (refreshToken == null || refreshToken.isBlank()) {
            if (this.accessToken == null || this.accessToken.isBlank()) {
                throw new IllegalStateException("Missing Zalo OA token. Set zalo.access-token or implement OAuth grant.");
            }
            this.expiresAtEpoch = now + 300;
            return;
        }

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("app_id", props.getAppId());
        form.add("grant_type", "refresh_token");
        form.add("refresh_token", refreshToken);

        // ĐỌC STRING + TỰ PARSE → bỏ hẳn bodyToMono(Map...)
        String body = webClient.post()
                .uri("/v4/oa/access_token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON, new MediaType("text","json"))
                .bodyValue(form)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        Map<String, Object> res;
        try {
            res = objectMapper.readValue(
                    body,
                    new com.fasterxml.jackson.core.type.TypeReference<Map<String,Object>>() {}
            );
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Cannot parse Zalo token response: " + body, e);
        }

        // Zalo thường có "error" == 0 nếu OK
        Integer error = (res.get("error") instanceof Number) ? ((Number) res.get("error")).intValue() : null;
        if (res.get("access_token") == null || (error != null && error != 0)) {
            throw new IllegalStateException("Refresh OA token failed: " + body);
        }

        this.accessToken   = String.valueOf(res.get("access_token"));
        this.refreshToken  = (String) res.getOrDefault("refresh_token", this.refreshToken);
        int expiresIn      = ((Number) res.getOrDefault("expires_in", 3600)).intValue();
        this.expiresAtEpoch = now + expiresIn;

        log.info("Zalo OA token refreshed. expiresIn={}s", expiresIn);
    }
}
