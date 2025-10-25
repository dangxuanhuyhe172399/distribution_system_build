package com.sep490.bads.distributionsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@Table(name = "Customer")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Customer extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String address;

    @ManyToOne @JoinColumn(name = "type_id")
    private CustomerType type;

    @Column(length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(name = "tax_code", length = 50)
    private String taxCode;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "customer") @JsonIgnore
    private List<SalesOrder> salesOrders;
}
