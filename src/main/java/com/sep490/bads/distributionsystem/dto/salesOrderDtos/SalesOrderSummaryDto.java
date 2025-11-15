package com.sep490.bads.distributionsystem.dto.salesOrderDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesOrderSummaryDto {

    // Đơn hàng
    private long totalOrders;      // Tổng đơn
    private long pendingOrders;    // Chờ xử lý (NEW + PENDING)
    private long shippingOrders;   // Đang giao (SHIPPED + DELIVERED)
    private long completedOrders;  // Hoàn thành (COMPLETED)

    private long returnRequests;   // Hoàn hàng
    private long exchangeRequests; // Đổi hàng
}
