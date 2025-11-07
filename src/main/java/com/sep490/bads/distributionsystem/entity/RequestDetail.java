package com.sep490.bads.distributionsystem.entity;

import com.sep490.bads.distributionsystem.entity.type.InspectionResultStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "RequestDetail", schema = "dbo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RequestDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_detail_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private Request request;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "saleorder_detail_id", nullable = false)
    private SalesOrderDetail orderDetail;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @Column(name="inspected_qty")
    private Long inspectedQty; // đạt

    @Enumerated(EnumType.STRING)
    @Column(name="inspection_result", length=20)
    private InspectionResultStatus inspectionResult;

    @Column(name = "reason_for_item", length = 255)
    private String reasonForItem;
}
