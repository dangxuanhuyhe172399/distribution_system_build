package com.sep490.bads.distributionsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Warehouse")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long warehouseId;

    @Column(name = "warehouse_name", length = 100)
    private String warehouseName;

    @Column(length = 255)
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User manager;

    @Column(columnDefinition = "bit default 1")
    private Boolean status = true;

    @OneToMany(mappedBy = "warehouse", fetch = FetchType.LAZY)
    private Set<Inventory> inventories = new HashSet<>();

    @OneToMany(mappedBy = "warehouse", fetch = FetchType.LAZY)
    private Set<ExportNote> exportNotes = new HashSet<>();
}