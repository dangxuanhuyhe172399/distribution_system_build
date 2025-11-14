package com.sep490.bads.distributionsystem.entity;

import com.sep490.bads.distributionsystem.entity.type.ContractDetailStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "ContractDetail", schema = "dbo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ContractDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contract_detail_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @Column(name = "unit_price", precision = 18, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "vat_amount", precision = 18, scale = 2)
    private BigDecimal vatAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private ContractDetailStatus status;

    @Column(name = "estimated_delivery_date")
    private LocalDate estimatedDeliveryDate;

    @Column(name = "note", length = 255)
    private String note;
}
