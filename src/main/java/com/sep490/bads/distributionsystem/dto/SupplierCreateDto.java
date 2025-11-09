package com.sep490.bads.distributionsystem.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierCreateDto {
    @NotBlank private String name;
    @Email private String email;
    @Pattern(regexp = "^[0-9]{9,15}$", message = "Số điện thoại không hợp lệ")
    private String phone;
    private String address;
    private String taxCode;
    @NotNull private Long categoryId;
}
