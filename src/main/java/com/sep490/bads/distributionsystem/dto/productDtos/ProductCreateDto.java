package com.sep490.bads.distributionsystem.dto.productDtos;

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
public class ProductCreateDto {
    @NotBlank @Size(max = 50)  private String sku;
    @NotBlank @Size(max = 100) private String name;
    @NotNull                  private Long categoryId;
    @NotNull                  private Long unitId;
    @DecimalMin("0") @Digits(integer=18, fraction=2) private BigDecimal costPrice;
    @DecimalMin("0") @Digits(integer=18, fraction=2) private BigDecimal sellingPrice;
    @Min(0) private Long minStock;
    @Min(0) private Long maxStock;
    private String description;   // Mô tả sản phẩm
    private String barcode;       // Mã vạch
    private String image;         // URL ảnh (nếu front upload riêng thì để link ở đây)
    private Long reorderQty;      // Số lượng đặt lại (nếu dùng)
    private String note;          // Ghi chú
}
