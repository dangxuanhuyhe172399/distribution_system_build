package com.sep490.bads.distributionsystem.dto.salesOrderDtos;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesOrderItemDto {
    private Long orderDetailId;
    private Long productId;
    private String productName;
    private Long quantity;
    private BigDecimal unitPrice;
    private BigDecimal discount;
    private BigDecimal vatAmount;
    private BigDecimal totalPrice;
}
