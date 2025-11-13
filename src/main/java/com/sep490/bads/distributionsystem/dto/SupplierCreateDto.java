package com.sep490.bads.distributionsystem.dto;


import jakarta.validation.constraints.*;
import lombok.*;

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