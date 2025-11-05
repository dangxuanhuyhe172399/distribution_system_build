package com.sep490.bads.distributionsystem.dto;

import com.sep490.bads.distributionsystem.entity.type.CommonStatus;
import lombok.*;
import jakarta.validation.constraints.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data @SuperBuilder
public class ProductUpdateDto {
    @Size(max = 50)  private String sku;
    @Size(max = 100) private String name;
    private Long categoryId;
    private Long unitId;
    @DecimalMin("0") @Digits(integer=18, fraction=2) private BigDecimal costPrice;
    @DecimalMin("0") @Digits(integer=18, fraction=2) private BigDecimal sellingPrice;
    @Min(0) private Long minStock;
    @Min(0) private Long maxStock;
    private CommonStatus status;
}
