package com.sep490.bads.distributionsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sep490.bads.distributionsystem.entity.type.ReviewStatus;
import com.sep490.bads.distributionsystem.entity.type.SaleOderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "SalesOrder", schema = "dbo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SalesOrder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "saleorder_code", length = 50, unique = true)
    private String saleOrderCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "total_amount", precision = 18, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "note", length = 255)
    private String note;

    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private SaleOderStatus status;

    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", updatable = false, nullable = false)
    private User createdBy;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<SalesOrderDetail> orderDetails;

    @OneToOne(mappedBy = "order")
    @JsonIgnore
    private Invoice invoice;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Request> requests;

    @Enumerated(EnumType.STRING)
    @Column(name = "finance_status", length = 20)
    private ReviewStatus financeStatus;     // PENDING / APPROVED

    @Enumerated(EnumType.STRING)
    @Column(name = "warehouse_status", length = 20)
    private ReviewStatus warehouseStatus;   // PENDING / APPROVED

    @Column(name = "progress_note", length = 500)
    private String progressNote;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<GoodsIssues> goodsIssues;
}
