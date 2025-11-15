package com.sep490.bads.distributionsystem.dto.productDtos;

import com.sep490.bads.distributionsystem.entity.type.ProductStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ProductFilterDto {
    private Long categoryId;
    private String keyword;
    private ProductStatus status;
    private BigDecimal priceFrom;
    private BigDecimal priceTo;

    // paging
    private int page = 0;
    private int size = 10;

    // sorting
    private String sortBy = "id";
    private String direction = "ASC";
}
