package com.sep490.bads.distributionsystem.config.security.auditing;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("springSecurityAuditorAware")
@RequiredArgsConstructor
public class SpringSecurityAuditorAware implements AuditorAware<Long> { // ← Long

    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal() == null) {
            return Optional.empty();
        }

        try {
            return Optional.of(Long.valueOf(auth.getName()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}