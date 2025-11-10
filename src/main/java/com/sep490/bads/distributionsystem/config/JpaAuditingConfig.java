package com.sep490.bads.distributionsystem.config;

import com.sep490.bads.distributionsystem.config.security.service.UserDetailsImpl;
import com.sep490.bads.distributionsystem.entity.User;
import com.sep490.bads.distributionsystem.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfig {

    @Bean
    public AuditorAware<User> auditorProvider(UserRepository userRepo) {
        return () -> {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()
                    || "anonymousUser".equals(auth.getPrincipal())) {
                return Optional.empty();
            }
            var principal = (UserDetailsImpl) auth.getPrincipal();
            return userRepo.findById(principal.getUserId());
        };
    }
}
