package com.sep490.bads.distributionsystem.dto.productDtos;

import com.sep490.bads.distributionsystem.entity.type.ProductStatus;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
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

    private ProductStatus status;
}
