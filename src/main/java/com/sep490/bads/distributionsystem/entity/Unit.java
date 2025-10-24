package com.sep490.bads.distributionsystem.entity;


import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "Unit")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Unit {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long unitId;


    @Column(name = "unit_name", length = 50, nullable = false)
    private String unitName;


    @OneToMany(mappedBy = "unit", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Product> products = new HashSet<>();
}