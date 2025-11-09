package com.sep490.bads.distributionsystem.dto.salesOrderDtos;

import lombok.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Data @NoArgsConstructor @AllArgsConstructor
public class SalesOrderItemCreateDto {
    @NotNull private Long productId;
    @NotNull @Positive private Long quantity;
    @NotNull @PositiveOrZero private BigDecimal unitPrice;
    @PositiveOrZero private BigDecimal discount;
    @PositiveOrZero private BigDecimal vatAmount;
}
