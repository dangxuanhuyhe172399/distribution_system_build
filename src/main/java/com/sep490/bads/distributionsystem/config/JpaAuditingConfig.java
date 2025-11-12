package com.sep490.bads.distributionsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider") // ← BẬT Auditing
public class JpaAuditingConfig {

    @Bean
    public AuditorAware<Long> auditorProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null) {
                return Optional.empty();
            }

            // Vì bạn dùng JWT với subject = userId → authentication.getName() chính là userId
            String userIdStr = authentication.getName();
            try {
                return Optional.of(Long.parseLong(userIdStr));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        };
    }
}