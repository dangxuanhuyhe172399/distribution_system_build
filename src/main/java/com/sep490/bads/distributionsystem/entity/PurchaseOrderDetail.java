package com.sep490.bads.distributionsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "PurchaseOrderDetail")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PurchaseOrderDetail extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "po_detail_id")
    private Long id;

    @ManyToOne @JoinColumn(name = "po_id")
    private PurchaseOrder purchaseOrder;

    @ManyToOne @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "unit_price", precision = 18)
    private Long unitPrice;

    @Column(name = "total_price", precision = 18, insertable = false, updatable = false)
    private Long totalPrice;
}