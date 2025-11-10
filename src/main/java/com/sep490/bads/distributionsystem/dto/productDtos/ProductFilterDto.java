package com.sep490.bads.distributionsystem.dto.productDtos;

import com.sep490.bads.distributionsystem.entity.type.ProductStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data @SuperBuilder
@NoArgsConstructor
public class ProductFilterDto {
    private Long categoryId;
    private String keyword;
    private ProductStatus status;
    private Long priceFrom;
    private Long priceTo;

     //paging
    private int page = 0;
    private int size = 10;

    // sorting
    private String sortBy = "id";
    private String direction = "ASC";
}
