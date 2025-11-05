package com.sep490.bads.distributionsystem.dto.inventoryDto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryDto {
    private Long productId;
    private String sku;
    private String productName;
    private String unitName;

    private Long warehouseId;
    private String warehouseName;

    private long availableQty;      // quantity - reserved_quantity
    private LocalDate nearestExpiry;

    private String badge;           // Còn hàng | Sắp hết hàng | Hết hàng | Ngừng kinh doanh
    private String productStatus;
    private String warehouseStatus;
}
