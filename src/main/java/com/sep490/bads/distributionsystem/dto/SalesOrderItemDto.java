package com.sep490.bads.distributionsystem.dto;

import lombok.*; import java.math.BigDecimal;

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
