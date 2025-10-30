package com.sep490.bads.distributionsystem.security.jwt;

import com.sep490.bads.distributionsystem.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenInfo {
    private Long userId;
    private String username;
    private String role;

    public static TokenInfo createFrom(User u) {
        return TokenInfo.builder()
                .userId(u.getId())
                .username(u.getUsername())
                .role(u.getRole() != null ? u.getRole().getRoleName() : null)
                .build();
    }

}

