package com.sep490.bads.distributionsystem.entity;


import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "Supplier")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Supplier {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supplierId;


    @Column(name = "supplier_name", length = 100, nullable = false)
    private String supplierName;


    @Column(name = "contact_name", length = 100)
    private String contactName;


    @Column(length = 20)
    private String phone;


    @Column(length = 100)
    private String email;


    @Column(length = 255)
    private String address;


    @Column(name = "tax_code", length = 50)
    private String taxCode;


    @Column(columnDefinition = "bit default 1")
    private Boolean status = true;


    @Column(name = "created_at")
    private LocalDateTime createdAt;


    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<PurchaseOrder> purchaseOrders = new HashSet<>();
}