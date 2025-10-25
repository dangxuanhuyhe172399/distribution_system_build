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
}
