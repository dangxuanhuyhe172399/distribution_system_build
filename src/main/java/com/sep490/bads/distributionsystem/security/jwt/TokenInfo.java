package com.sep490.bads.distributionsystem.security.jwt;

import com.sep490.bads.distributionsystem.entity.User;
import lombok.Data;

@Data
public class TokenInfo {
    private Long userId;

    public static TokenInfo createFrom(User user) {
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setUserId(user.getId());
        return tokenInfo;
    }

}

