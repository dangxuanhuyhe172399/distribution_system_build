package com.sep490.bads.distributionsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Invoice")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", unique = true)
    private SalesOrder order;

    @Column(name = "invoice_date")
    private LocalDateTime invoiceDate;

    @Column(name = "vat_rate", precision = 5)
    private Long vatRate;

    @Column(name = "vat_amount", precision = 18)
    private Long vatAmount;

    @Column(name = "grand_total", precision = 18)
    private Long grandTotal;

    @Builder.Default
    @Column(name = "status", length = 20)
    private String status = "Pending";

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
}
