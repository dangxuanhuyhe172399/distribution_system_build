package com.sep490.bads.distributionsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@Table(name = "Supplier")
@NoArgsConstructor @AllArgsConstructor @Builder
public class Supplier extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id")
    private Long id;

    @Column(name = "supplier_name", nullable = false, length = 100)
    private String supplierName;

    @Column(name = "contact_name", length = 100)
    private String contactName;

    @Column(name = "phone", length = 20)
    private String phone;


    @Column(name = "email", length = 100)
    private String email;


    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "tax_code", length = 50)
    private String taxCode;

    @Column(name = "status")
    private Boolean status;


    @OneToMany(mappedBy = "supplier") @JsonIgnore
    private List<PurchaseOrder> purchaseOrders;
}
