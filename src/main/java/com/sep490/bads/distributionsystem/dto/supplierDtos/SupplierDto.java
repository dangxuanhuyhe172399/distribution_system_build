package com.sep490.bads.distributionsystem.dto.supplierDtos;

import com.sep490.bads.distributionsystem.entity.SupplierCategory;
import com.sep490.bads.distributionsystem.entity.type.SupplierStatus;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupplierDto {
    private Long id;
    private String name;         // Tên NCC
    private String contactName;  // Người liên hệ
    private String phone;
    private String email;
    private String address;
    private String taxCode;
    private SupplierCategory category; // Bao bì / Sản phẩm / Nguyên liệu...
    private SupplierStatus status;
}
