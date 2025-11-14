package com.sep490.bads.distributionsystem.dto.supplierDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupplierStatsDto {
    private BigDecimal totalPurchase;        // Tổng mua
    private BigDecimal outstandingDebt;      // Công nợ
    private BigDecimal totalPaid;           // Đã thanh toán

    // Thông tin hợp tác
    private BigDecimal onTimePaymentRate;    // %
    private Long orderCount;                // Số đơn hàng
    private BigDecimal avgOrderValue;       // Giá trị đơn hàng trung bình
    private Long cooperationMonths;         // Thời gian hợp tác (tháng)
}
