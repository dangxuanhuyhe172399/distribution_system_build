package com.sep490.bads.distributionsystem.dto.inventoryDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class InventoryFilterDto {
    private Long warehouseId;
    private String q;        // SKU hoặc tên sản phẩm
    private String status;   // IN_STOCK | LOW_STOCK | OUT_OF_STOCK | DISCONTINUED | NEAR_EXPIRY
    private Integer expireWithinDays; // mặc định 14 nếu null
}
