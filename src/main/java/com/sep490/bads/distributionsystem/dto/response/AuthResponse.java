package com.tal.recruitment.system.dto.response;

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
    private String token;
    private String tokenType;
    private Long userId;
    private String userEmail;
    private String role;
    private String fullname;
    private Instant previousLastLoginAt;
}
