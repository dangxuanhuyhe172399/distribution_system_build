package com.sep490.bads.distributionsystem.dto;

import lombok.*;

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

    private Boolean status;
}
