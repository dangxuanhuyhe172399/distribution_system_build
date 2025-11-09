package com.sep490.bads.distributionsystem.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@Table(name = "SupplierCategory", schema = "dbo")
@AllArgsConstructor
@NoArgsConstructor
public class SupplierCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_category_id")
    private Long id;

    @Column(name = "category_name", length = 100, nullable = false)
    private String name;

    @Column(name = "description", length = 255)
    private String description;
}
