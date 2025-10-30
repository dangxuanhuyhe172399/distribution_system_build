package com.sep490.bads.distributionsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesOrderItemDto {
    private Long productId;
    private String productName;
    private Long quantity;
    private Long unitPrice;
    private Long discount;
    private Long totalPrice;
}
