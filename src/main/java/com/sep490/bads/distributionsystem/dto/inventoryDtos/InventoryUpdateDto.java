package com.sep490.bads.distributionsystem.dto.inventoryDtos;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class InventoryUpdateDto {
    private Integer qrId;
    @Min(0) private Long quantity;
    @Min(0) private Long reservedQuantity;
    @Min(0) private Long safetyStock;

    private LocalDate manufactureDate;
    private LocalDate expiryDate;
}
