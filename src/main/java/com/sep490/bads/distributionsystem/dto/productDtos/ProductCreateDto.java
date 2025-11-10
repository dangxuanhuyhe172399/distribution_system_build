package com.sep490.bads.distributionsystem.dto.productDtos;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@SuperBuilder
public class ProductCreateDto {
    @NotBlank @Size(max = 50)  private String sku;
    @NotBlank @Size(max = 100) private String name;
    @NotNull                  private Long categoryId;
    @NotNull                  private Long unitId;
    @DecimalMin("0") @Digits(integer=18, fraction=2) private BigDecimal costPrice;
    @DecimalMin("0") @Digits(integer=18, fraction=2) private BigDecimal sellingPrice;
    @Min(0) private Long minStock;
    @Min(0) private Long maxStock;
}
