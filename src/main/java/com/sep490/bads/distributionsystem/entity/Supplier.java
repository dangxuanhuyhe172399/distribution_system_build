package com.sep490.bads.distributionsystem.entity;

import com.sep490.bads.distributionsystem.entity.type.CommonStatus;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Supplier")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Supplier extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "tax_code", length = 50)
    private String taxCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    @Builder.Default
    private CommonStatus status = CommonStatus.ACTIVE;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private SupplierCategory category;

    @OneToMany(mappedBy = "supplier")
    @JsonIgnore
    private List<Product> products; // nếu sản phẩm có quan hệ với nhà cung cấp
}
