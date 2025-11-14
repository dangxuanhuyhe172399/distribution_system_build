package com.sep490.bads.distributionsystem.dto.productDtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sep490.bads.distributionsystem.entity.*;
import com.sep490.bads.distributionsystem.entity.type.ProductStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ProductDto {
    private Long id;
    private String sku;
    private String name;
    private String description;
    private String barcode;
    private String image;
    private Long categoryId;
    private String categoryName;

    private Long costPrice;
    private Long sellingPrice;

    private Long stockQuantity;
    private Long minStock;
    private Long maxStock;

    private ProductStatus status;
    private ProductCategory category;
    private Unit unit;
    private User createdBy;
    private String note;
    private Long reorderQty;
    private List<Inventory> inventories;
    private List<Qrcode> qrcodes;
}
