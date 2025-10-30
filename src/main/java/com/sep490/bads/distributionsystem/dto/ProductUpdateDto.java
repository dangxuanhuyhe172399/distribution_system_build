package com.sep490.bads.distributionsystem.dto;

import com.sep490.bads.distributionsystem.entity.type.CommonStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private CommonStatus status;
    @NotBlank
    @Size(max = 50)
    private String sku;
}
