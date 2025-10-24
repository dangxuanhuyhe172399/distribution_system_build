package com.sep490.bads.distributionsystem.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {

    @Schema(description = "Token được trả về")
    private String accessToken;

    @Schema(description = "Vai trò của tài khoản liên quan đến token")
    private Set<String> role;

    @Schema(description = "Token được trả về sau refresh")
    private String refreshToken;
}
