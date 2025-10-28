package com.sep490.bads.distributionsystem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Inventory")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Inventory extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Long id;

    @ManyToOne @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity")
    private Long quantity;
}
