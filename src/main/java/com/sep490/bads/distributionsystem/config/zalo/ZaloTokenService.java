package com.sep490.bads.distributionsystem.config.zalo;

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

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ZaloTokenService {

    @Qualifier("zaloOauthWebClient")
    private final WebClient webClient;           // baseUrl = https://oauth.zaloapp.com
    private final ZaloProperties props;

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
        if (accessToken != null && now < (expiresAtEpoch - 60)) return; // ai đó đã refresh

        if (refreshToken == null || refreshToken.isBlank()) {
            // Không có refresh_token -> dùng luôn access_token hiện có (dev tool)
            if (this.accessToken == null || this.accessToken.isBlank()) {
                throw new IllegalStateException("Missing Zalo OA token. Set zalo.access-token or implement OAuth grant.");
            }
            this.expiresAtEpoch = now + 300; // tránh refresh loop
            return;
        }

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("app_id", props.getAppId());
        form.add("grant_type", "refresh_token");
        form.add("refresh_token", refreshToken);

        Map<String, Object> res = webClient.post()
                .uri("https://oauth.zaloapp.com/v4/oa/access_token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(form)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String,Object>>() {})
                .block();

        if (res == null || !res.containsKey("access_token")) {
            throw new IllegalStateException("Refresh OA token failed: " + res);
        }
        this.accessToken   = String.valueOf(res.get("access_token"));
        this.refreshToken  = (String) res.getOrDefault("refresh_token", this.refreshToken);
        int expiresIn      = ((Number) res.getOrDefault("expires_in", 3600)).intValue();
        this.expiresAtEpoch = now + expiresIn;
    }
}
