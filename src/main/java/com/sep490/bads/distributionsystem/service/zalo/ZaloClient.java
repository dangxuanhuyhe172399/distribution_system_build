package com.sep490.bads.distributionsystem.service.zalo;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ZaloClient {
    private final WebClient web = WebClient.builder().build();

    // Ví dụ: thay URL/param đúng theo tài liệu Shop API của bạn
    public Mono<String> getOrders(String accessToken, String from, String to, String status, int offset, int limit) {
        return web.get()
                .uri(uri -> uri
                        .scheme("https")
                        .host("openapi.zalo.me")
                        .path("/v2.0/shop/order/getlist") // thay đúng endpoint
                        .queryParam("from_time", from)
                        .queryParam("to_time", to)
                        .queryParam("status", status)
                        .queryParam("offset", offset)
                        .queryParam("limit", limit)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .header("access_token", accessToken) // hoặc bearer
                .retrieve()
                .bodyToMono(String.class);
    }
}
