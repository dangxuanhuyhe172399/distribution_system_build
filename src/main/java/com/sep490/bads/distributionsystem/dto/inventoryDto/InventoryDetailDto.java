package com.sep490.bads.distributionsystem.dto.inventoryDto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class InventoryDetailDto {
    private Long productId;
    private String sku;
    private String productName;

    private Long warehouseId;
    private String warehouseName;

    private String unitName;
    private long availableQty;          // tổng tồn khả dụng

    private LocalDate manufactureDate;  // của lô gần hết hạn
    private LocalDate expiryDate;       // lô gần hết hạn
    private Long daysToExpire;          // DATEDIFF(day, GETDATE, expiry)

    private BigDecimal sellingPrice;    // đơn giá bán (Product.selling_price)
    private String supplierName;        // NCC từ lần nhập gần nhất (nếu có)
}
