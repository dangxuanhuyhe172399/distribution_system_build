package com.sep490.bads.distributionsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sep490.bads.distributionsystem.entity.type.CommonStatus;

@Entity
@Table(name = "Product", schema = "dbo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "sku", length = 50, unique = true, nullable = false)
    private String sku;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "barcode", length = 64)
    private String barcode;

    @Column(name = "image", length = 255)
    private String image;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "cost_price", precision = 18, scale = 2)
    private Long costPrice;

    @Column(name = "selling_price", precision = 18, scale = 2)
    private Long sellingPrice;

    @Column(name = "min_stock")
    private Long minStock;

    @Column(name = "max_stock")
    private Long maxStock;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private CommonStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private ProductCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id")
    private Unit unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "note", length = 255)
    private String note;

    @Column(name = "reorder_qty")
    private Long reorderQty;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Inventory> inventories;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Qrcode> qrcodes;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

}
