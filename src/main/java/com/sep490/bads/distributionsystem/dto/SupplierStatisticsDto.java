package com.sep490.bads.distributionsystem.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierStatisticsDto {
    private Long totalOrders;  // Tổng số đơn mua hàng
    private Long totalDebt;  // Tổng công nợ
    private Long totalPaid;  // Tổng tiền đã thanh toán
}
