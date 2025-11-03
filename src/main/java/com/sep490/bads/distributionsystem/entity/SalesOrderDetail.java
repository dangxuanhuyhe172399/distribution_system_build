package com.sep490.bads.distributionsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "SalesOrderDetail", schema = "dbo")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SalesOrderDetail extends BaseEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Long id;

    @ManyToOne @JoinColumn(name = "order_id")
    private SalesOrder order;

    @ManyToOne @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "unit_price", precision = 18)
    private Long unitPrice;

    @Column(name = "discount", precision = 5)
    private Long discount;

    @Column(name = "total_price", precision = 18, insertable = false, updatable = false)
    private Long totalPrice;
}
