package com.sep490.bads.distributionsystem.security.jwt;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenInfo {
    private Long userId;
    private String username;
    private String[] roles;
}