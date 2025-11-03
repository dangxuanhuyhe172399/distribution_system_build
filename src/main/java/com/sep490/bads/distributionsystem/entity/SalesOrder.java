package com.sep490.bads.distributionsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "SalesOrder", schema = "dbo")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SalesOrder extends BaseEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Column(name = "note", length = 255)
    private String note;

    @OneToMany(mappedBy = "order")
    private List<SalesOrderDetail> details;

    @OneToOne(mappedBy = "order")
    @JsonIgnore
    private Invoice invoice;
}