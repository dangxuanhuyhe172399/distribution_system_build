package com.sep490.bads.distributionsystem.dto.CustomerDto;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerInsightDto {
    // header
    private BigDecimal totalPurchased; // Tổng đã mua (sum total_amount các đơn hợp lệ)
    private Long       orderCount;     // Số đơn
    private BigDecimal currentBalance; // Số dư hiện tại (từ Customer.currentBalance)

    // list giao dịch (gần nhất)
    private List<CustomerOrderSummaryDto> recentOrders;
}
