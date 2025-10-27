package com.sep490.bads.distributionsystem.dto;

import lombok.*;
import java.math.BigDecimal;

@Data @NoArgsConstructor @AllArgsConstructor
public class ProductFilterDto {
    private Long categoryId;
    private String keyword;
    private Boolean status;
    private Long priceFrom;
    private Long priceTo;

     //paging
    private int page = 0;
    private int size = 10;

    // sorting
    private String sortBy = "id";
    private String direction = "ASC";
}
