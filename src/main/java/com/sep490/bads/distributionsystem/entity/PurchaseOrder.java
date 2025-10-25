package com.sep490.bads.distributionsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "PurchaseOrder")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PurchaseOrder extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "po_id")
    private Long id;

    @ManyToOne @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @ManyToOne @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "po_date")
    private LocalDateTime poDate;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "note", length = 255)
    private String note;

    @OneToMany(mappedBy = "purchaseOrder")
    private List<PurchaseOrderDetail> details;
}