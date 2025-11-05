package com.sep490.bads.distributionsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Qrcode", schema = "dbo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Qrcode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qr_id")
    private Integer id;

    @Column(name = "qr_image", length = 255, nullable = false)
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    @JsonIgnore
    private Warehouse warehouse;
}
