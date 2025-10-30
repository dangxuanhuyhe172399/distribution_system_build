package com.sep490.bads.distributionsystem.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class SalesOrderItemCreateDto {
    @NotNull
    private Long productId;
    @Positive
    private Long quantity;
    @PositiveOrZero
    private Long unitPrice;
    @PositiveOrZero
    private Long discount; // 0..1 hoặc tiền tệ tùy bạn định nghĩa
}
