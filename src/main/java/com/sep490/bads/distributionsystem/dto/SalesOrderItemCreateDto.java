package com.sep490.bads.distributionsystem.dto;

import jakarta.validation.constraints.*;
import lombok.*;

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
