package com.sep490.bads.distributionsystem.dto.supplierDtos;

import com.sep490.bads.distributionsystem.entity.type.SupplierTransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupplierTransactionDto {
    private LocalDate date;                  // Ngày
    private SupplierTransactionType type;    // Mua hàng / Thanh toán
    private String description;              // Mô tả
    private String referenceCode;            // PO-2025-001 / PAY-001
    private BigDecimal amount;              // Số tiền
    private String status;                  // Hoàn thành / Đang xử lý...
}
