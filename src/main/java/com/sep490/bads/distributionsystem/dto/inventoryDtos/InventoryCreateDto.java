package com.sep490.bads.distributionsystem.dto.inventoryDtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class InventoryCreateDto {
    @NotNull private Long warehouseId;
    @NotNull private Long productId;
    private Integer qrId;                      // optional

    @Min(0) @NotNull private Long quantity;
    @Min(0) private Long reservedQuantity = 0L;
    @Min(0) private Long safetyStock;         // optional

    private LocalDate manufactureDate;
    private LocalDate expiryDate;
}
