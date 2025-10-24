package com.sep490.bads.distributionsystem.entity;


import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "Customer")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Customer {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;


    @Column(nullable = false, length = 100)
    private String name;


    @Column(length = 255)
    private String address;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private CustomerType customerType;


    @Column(length = 100)
    private String email;


    @Column(length = 20)
    private String phone;


    @Column(name = "tax_code", length = 50)
    private String taxCode;


    @Column(name = "created_at")
    private LocalDateTime createdAt;


    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private Set<SalesOrder> salesOrders = new HashSet<>();
}