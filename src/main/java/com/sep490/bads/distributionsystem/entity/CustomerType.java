package com.sep490.bads.distributionsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@Table(name = "CustomerType")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CustomerType extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id")
    private Long id;

    @Column(name = "type_name", length = 50)
    private String name;

    @OneToMany(mappedBy = "type") @JsonIgnore
    private List<Customer> customers;
}
