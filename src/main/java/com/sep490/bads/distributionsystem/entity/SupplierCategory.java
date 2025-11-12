package com.sep490.bads.distributionsystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "SupplierCategory", schema = "dbo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "s_category_id")
    private Long id;

    @Column(name = "s_category_name", nullable = false, length = 100)
    private String name;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Quan hệ 1-nhiều với Supplier
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Supplier> suppliers;


}
