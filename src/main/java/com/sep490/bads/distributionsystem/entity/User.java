package com.sep490.bads.distributionsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "User")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(nullable = false)
    private Boolean status = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;


    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<SalesOrder> salesOrders = new HashSet<>();


    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<PurchaseOrder> purchaseOrders = new HashSet<>();


    @OneToMany(mappedBy = "createdByUser", fetch = FetchType.LAZY)

    private Set<Invoice> invoicesCreated = new HashSet<>();


    @OneToMany(mappedBy = "createdByUser", fetch = FetchType.LAZY)
    private Set<ExportNote> exportNotesCreated = new HashSet<>();
}

