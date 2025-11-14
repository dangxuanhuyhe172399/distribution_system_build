package com.sep490.bads.distributionsystem.dto.purchaseOrderDtos;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderItemDto {
    private Long productId;
    private String productName;
    private Long quantity;
    private BigDecimal unitPrice;
    private BigDecimal vatAmount;
    private BigDecimal lineTotal; // quantity * unitPrice (+ vat)
    private LocalDate estimatedDeliveryDate;
    private Boolean archived;
}
