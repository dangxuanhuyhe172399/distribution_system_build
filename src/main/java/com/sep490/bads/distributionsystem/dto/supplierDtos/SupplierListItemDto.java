package com.sep490.bads.distributionsystem.dto.supplierDtos;

import com.sep490.bads.distributionsystem.entity.type.SupplierStatus;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class SupplierListItemDto {
    private Long id;
    private String code;          // "NCC001" (render từ id)
    private String name;          // tên NCC
    private String contactName;
    private String phone;         // Liên hệ
    private String taxCode;       // Mã số thuế
    private String categoryName;  // Loại (Bao bì, Nguyên liệu...)
    private BigDecimal outstandingDebt;      // Công nợ
    private SupplierStatus status;
}
