package com.sep490.bads.distributionsystem.config.zalo;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ZaloApiClient {

    private final ZaloTokenService zaloTokenService;
    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://openapi.zalo.me") // base mặc định
            .build();

    @Value("${zalo.access-token}")   private String accessToken;   // OA access token
    @Value("${zalo.api.ver:3.0}")    private String apiVer;        // 2.0 hoặc 3.0

    /** 1) Gửi text đơn giản */
    public Mono<Map<String, Object>> sendText(String userId, String text) {
        var body = Map.of(
                "recipient", Map.of("user_id", userId),
                "message",   Map.of("text", text)
        );
        return postJson("/v" + apiVer + "/oa/message", body);
    }

    /** 2) Gửi CTA “Chia sẻ thông tin” để kích hoạt user_share_info */
    public Mono<Map<String, Object>> sendRequestShareInfo(String userId,
                                                          String title,
                                                          String subtitle) {
        // Có 2 kiểu payload phổ biến tuỳ version. Giữ cả 2; thử A, nếu 400 thì thử B.
        var payloadA = Map.of(
                "recipient", Map.of("user_id", userId),
                "message", Map.of(
                        "text", title,
                        "attachment", Map.of(
                                "type", "template",
                                "payload", Map.of(
                                        "template_type", "request_user_info",
                                        "elements", new Object[]{
                                                Map.of("title", title, "subtitle", subtitle)
                                        }
                                )
                        )
                )
        );

        // Phương án B: dùng button action (nếu OA bạn dùng kiểu “action”)
        var payloadB = Map.of(
                "recipient", Map.of("user_id", userId),
                "message", Map.of(
                        "text", title,
                        "buttons", new Object[]{
                                Map.of(
                                        "title", "Chia sẻ thông tin",
                                        // Một số OA dùng "oa.request.user.info", số khác dùng "oa.query.hideInfo".
                                        "type",  "oa.request.user.info"
                                )
                        }
                )
        );

        // thử A trước, nếu fail sẽ fallback sang B
        return postJson("/v" + apiVer + "/oa/message", payloadA)
                .onErrorResume(ex -> postJson("/v" + apiVer + "/oa/message", payloadB));
    }

    /** 3) Lấy list hội thoại gần nhất (populate combobox) */
    public Mono<Map<String, Object>> getRecentConversations(int offset, int count) {
        var body = Map.of("offset", offset, "count", count);
        // Một số OA là /oa/conversation/getrecent, một số là /oa/conversation
        return postJson("/v" + apiVer + "/oa/conversation", Map.of("data", body));
    }

    /** 4) Lấy profile theo user_id (bổ sung avatar/name nếu cần) */
    public Mono<Map<String, Object>> getUserProfile(String userId) {
        var body = Map.of("user_id", userId);
        return postJson("/v2.0/oa/getprofile", Map.of("data", body)); // đường cũ của OA, vẫn hoạt động
    }

    // ZaloApiClient.java
    private Mono<Map<String, Object>> postJson(String path, Object body) {
        return webClient.post()
                .uri(path)
                .header("access_token", zaloTokenService.getAccessToken())   // << có tokenService ở mục 2
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    private Mono<Map<String, Object>> getJson(String path) {
        return webClient.get()
                .uri(path)
                .header("access_token", zaloTokenService.getAccessToken())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

}
