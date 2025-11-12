package com.sep490.bads.distributionsystem.config.zalo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ZaloWebClientConfig {

    @Bean
    public WebClient zaloOauthWebClient(WebClient.Builder builder) {
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer ->
                        configurer.defaultCodecs().jackson2JsonDecoder(
                                new Jackson2JsonDecoder(new ObjectMapper(),
                                        MediaType.APPLICATION_JSON,
                                        new MediaType("text", "json")
                                )
                        )
                )
                .build();

        return builder
                .baseUrl("https://oauth.zaloapp.com")
                .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
                .exchangeStrategies(strategies)
                .build();
    }
}


