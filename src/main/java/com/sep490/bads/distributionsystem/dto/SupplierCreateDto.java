package com.sep490.bads.distributionsystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierCreateDto {

    @NotBlank
    private String name;

    private String contactName;

    @NotBlank
    private String phone;

    @Email
    private String email;

    private String address;

    private String taxCode;

    @NotNull(message = "Danh mục nhà cung cấp không được để trống")
    private Long categoryId;
}
