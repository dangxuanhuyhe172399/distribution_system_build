package com.sep490.bads.distributionsystem.config.zalo;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ZaloZnsClient {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://business.openapi.zalo.me") // base ZNS
            .build();

  //  @Value("${zns.access-token}")
    private String znsAccessToken;

   // @Value("${zns.order-status-template-id}")
    private String orderStatusTemplateId;

    public Mono<Map<String, Object>> sendOrderStatus(
            String phone84,
            String orderCode,
            String status,
            long totalAmount
    ) {
        Map<String, Object> templateData = Map.of(
                "order_code",  orderCode,
                "status",      status,
                "total",       String.valueOf(totalAmount)
        );

        Map<String, Object> body = Map.of(
                "phone",        phone84,                 // dạng 84xxxxxxxxx
                "template_id",  orderStatusTemplateId,
                "template_data", templateData,
                "tracking_id",  "ORD-" + orderCode
        );

        return webClient.post()
                .uri("/message/template") // endpoint thật xem trong docs ZNS
                .header("access_token", znsAccessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
    }
}

