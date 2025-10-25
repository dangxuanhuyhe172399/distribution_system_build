package com.sep490.bads.distributionsystem.dto;

import lombok.*;
import jakarta.validation.constraints.*;

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
