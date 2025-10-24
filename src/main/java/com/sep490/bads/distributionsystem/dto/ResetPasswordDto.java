package com.sep490.bads.distributionsystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordDto {
    @NotBlank
    private String oldPassword;
    @NotBlank
    private String newPassword;
}
