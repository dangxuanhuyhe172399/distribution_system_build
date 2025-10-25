package com.sep490.bads.distributionsystem.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private Long categoryId;
    private String categoryName;
    private Long unitId;
    private String unitName;
    private Long costPrice;
    private Long sellingPrice;
    private Long stockQuantity;
    private Boolean status;
    private String createdAt;
}
