package com.sep490.bads.distributionsystem.entity;


import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "CustomerType")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerType {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long typeId;


    @Column(name = "type_name", length = 50)
    private String typeName;


    @OneToMany(mappedBy = "customerType", fetch = FetchType.LAZY)
    private Set<Customer> customers = new HashSet<>();
}