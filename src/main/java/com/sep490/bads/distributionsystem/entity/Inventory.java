package com.sep490.bads.distributionsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import com.sep490.bads.distributionsystem.entity.type.CommonStatus;

@Entity
@Table(name = "Inventory", schema = "dbo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qr_id")
    private Qrcode qrcode;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "reserved_quantity")
    private Long reservedQuantity;

    @Column(name = "safety_stock")
    private Long safetyStock;

    @Column(name = "manufacture_date")
    private LocalDate manufactureDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "last_in_at")
    private LocalDate lastInAt;

    @Column(name = "last_out_at")
    private LocalDate lastOutAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private CommonStatus status;
}
