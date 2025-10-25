package com.sep490.bads.distributionsystem.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data @NoArgsConstructor @AllArgsConstructor
public class ProductUpdateDto {
    @NotBlank @Size(max=100) private String name;
    @NotNull
    private Long categoryId;
    @NotNull
    private Long unitId;
    @PositiveOrZero
    private Long costPrice;
    @PositiveOrZero
    private Long sellingPrice;
    @PositiveOrZero
    private Long minStock;
    @PositiveOrZero
    private Long maxStock;
    private Boolean status;
}
