package com.sep490.bads.distributionsystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoodsReceiptDetailCreateDto {
    @NotNull private Long productId;
    @NotNull private Long quantity;
    private LocalDate manufactureDate;
    private LocalDate expiryDate;
    private String note;
}
