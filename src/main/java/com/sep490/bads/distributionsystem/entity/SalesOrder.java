package com.sep490.bads.distributionsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "SalesOrder")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SalesOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(length = 50)
    private String status;

    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Column(length = 255)
    private String note;

    @OneToMany(mappedBy = "salesOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<SalesOrderDetail> details = new HashSet<>();

    @OneToOne(mappedBy = "salesOrder", fetch = FetchType.LAZY)
    private Invoice invoice;
}