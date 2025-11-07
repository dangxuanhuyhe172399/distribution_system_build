package com.sep490.bads.distributionsystem.config.zalo;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter @Setter
@Configuration
@ConfigurationProperties(prefix = "zalo")
public class ZaloProperties {
    private String appId;
    private String oaSecret;
    private boolean verify = true;
    private String signatureMode = "APP_BODY_TS";
    private String webhookPath = "/v1/public/zalo/webhook";
}
