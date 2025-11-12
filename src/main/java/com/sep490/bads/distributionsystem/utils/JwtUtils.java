// src/main/java/com/sep490/bads/distributionsystem/utils/JwtUtils.java
package com.sep490.bads.distributionsystem.utils;

import com.sep490.bads.distributionsystem.config.security.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    @Autowired
    private static JwtService jwtService;

    @Autowired
    public void setJwtService(JwtService jwtService) {
        JwtUtils.jwtService = jwtService;
    }

    public static Long getCurrentUserId() {
        try {
            String token = (String) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getCredentials();

            String userIdStr = jwtService.extractId(token);
            return Long.parseLong(userIdStr);
        } catch (Exception e) {
            return 1L; // user hệ thống khi test hoặc lỗi
        }
    }
}