package com.sep490.bads.distributionsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sep490.bads.distributionsystem.entity.type.CommonStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Product")
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

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @ManyToOne @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne @JoinColumn(name = "unit_id")
    private Unit unit;

    @Column(name = "cost_price", precision = 18)
    private Long costPrice;

    @Column(name = "selling_price", precision = 18)
    private Long sellingPrice;

    @Column(name = "stock_quantity")
    private Long stockQuantity;

    @Column(name = "min_stock")
    private Long minStock;

    @Column(name = "max_stock")
    private Long maxStock;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private CommonStatus status;

    @Column(name = "sku", length = 50, nullable = false, unique = true)
    private String sku;

    @OneToMany(mappedBy = "product") @JsonIgnore
    private List<SalesOrderDetail> salesDetails;

    @OneToMany(mappedBy = "product") @JsonIgnore
    private List<PurchaseOrderDetail> purchaseDetails;

    @OneToMany(mappedBy = "product") @JsonIgnore
    private List<Inventory> inventories;
}
