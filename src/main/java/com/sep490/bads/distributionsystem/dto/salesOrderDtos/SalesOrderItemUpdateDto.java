package com.sep490.bads.distributionsystem.dto.salesOrderDtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesOrderItemUpdateDto {
    private Long orderDetailId;            // null => thêm mới
    @NotNull
    private Long productId;
    @NotNull @Positive
    private Long quantity;
    @NotNull @PositiveOrZero
    private BigDecimal unitPrice;
    @PositiveOrZero private BigDecimal discount;
    @PositiveOrZero private BigDecimal vatAmount;
}
