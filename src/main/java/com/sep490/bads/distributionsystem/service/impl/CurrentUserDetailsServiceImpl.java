// CurrentUserDetailsServiceImpl.java
package com.sep490.bads.distributionsystem.service.impl;

import com.sep490.bads.distributionsystem.config.security.service.UserDetailsImpl;
import com.sep490.bads.distributionsystem.entity.User;
import com.sep490.bads.distributionsystem.repository.UserRepository;
import com.sep490.bads.distributionsystem.service.CurrentUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserDetailsServiceImpl implements CurrentUserDetailsService {

    private final UserRepository userRepository;

    @Override
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getPrincipal() instanceof UserDetailsImpl userDetails) {
            // DÙNG getUsername() để lấy username → tìm User trong DB
            return userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }

        throw new RuntimeException("No authenticated user");
    }
}