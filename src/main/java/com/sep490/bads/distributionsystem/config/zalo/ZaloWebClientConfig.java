package com.sep490.bads.distributionsystem.config.zalo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ZaloWebClientConfig {

    @Bean
    public WebClient zaloOauthWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://oauth.zaloapp.com")
                .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

}
