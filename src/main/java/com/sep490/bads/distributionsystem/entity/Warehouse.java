package com.sep490.bads.distributionsystem.entity;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Warehouse")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Warehouse extends BaseEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warehouse_id")
    private Long id;

    @Column(name = "warehouse_name", length = 100)
    private String name;

    @Column(name = "address", length = 255)
    private String address;

    @ManyToOne @JoinColumn(name = "manager_id")
    private User manager;

    @Column(name = "status")
    private Boolean status;

    @OneToMany(mappedBy = "warehouse") @JsonIgnore
    private List<Inventory> inventories;

    @OneToMany(mappedBy = "warehouse") @JsonIgnore
    private List<ExportNote> exportNotes;
}