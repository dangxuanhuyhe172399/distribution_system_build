package com.sep490.bads.distributionsystem.dto.productDtos;

import com.sep490.bads.distributionsystem.entity.type.ProductStatus;
import lombok.*;
import jakarta.validation.constraints.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ProductUpdateDto {
    @Size(max = 50)  private String sku;
    @Size(max = 100) private String name;
    private Long categoryId;
    private Long unitId;
    @DecimalMin("0") @Digits(integer=18, fraction=2) private BigDecimal costPrice;
    @DecimalMin("0") @Digits(integer=18, fraction=2) private BigDecimal sellingPrice;
    @Min(0) private Long minStock;
    @Min(0) private Long maxStock;
    private ProductStatus status;
    @Size(max = 255)
    private String description;      // Mô tả sản phẩm
    @Size(max = 64)
    private String barcode;          // Mã vạch
    @Size(max = 255)
    private String image;            // URL ảnh
    @Size(max = 255)
    private String note;             // Ghi chú
    @Min(0)
    private Long reorderQty;         // Số lượng đặt lại
}
