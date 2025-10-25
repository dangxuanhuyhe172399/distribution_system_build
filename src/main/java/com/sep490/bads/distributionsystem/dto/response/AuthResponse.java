package com.sep490.bads.distributionsystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String token;          // access token
    private String refreshToken;   // optional
    private String tokenType;      // "Bearer"
    private Long userId;
    private String userEmail;
    private String userName;
    private String fullName;
    private String role;           // roleName
    private Instant expiresAt;
}
