package com.sep490.bads.distributionsystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordOtpDto {
    @NotBlank
    private String email;
    @NotBlank
    private String otp;
    @NotBlank
    private String newPassword;
}
