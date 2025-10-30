package com.sep490.bads.distributionsystem.dto;

import com.sep490.bads.distributionsystem.entity.type.CommonStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String sku;
    private String name;

    private Long categoryId;
    private String categoryName;

    private Long unitId;
    private String unitName;

    private Long costPrice;
    private Long sellingPrice;

    private Long stockQuantity;
    private Long minStock;
    private Long maxStock;

    private CommonStatus status;
}
