package com.sep490.bads.distributionsystem.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierCreateDto {
    @NotBlank(message = "Tên nhà cung cấp không được để trống")
    private String name;

    private String address;

    @NotNull(message = "Phải chọn danh mục nhà cung cấp")
    private Long categoryId;

    @Email(message = "Email không hợp lệ")
    private String email;

    @Pattern(regexp = "^[0-9]{9,15}$", message = "Số điện thoại không hợp lệ")
    private String phone;

    private String taxCode;
}
