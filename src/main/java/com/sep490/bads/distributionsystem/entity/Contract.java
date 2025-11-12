package com.sep490.bads.distributionsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sep490.bads.distributionsystem.entity.type.CommonStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "Contract", schema = "dbo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Contract extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contract_id")
    private Long id;

    @Column(name = "contract_code", length = 50, unique = true)
    private String contractCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private CommonStatus status;

    @Column(name = "note", length = 255)
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @OneToMany(mappedBy = "contract", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ContractDetail> contractDetails;

    @OneToMany(mappedBy = "contract", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<GoodsReceipt> goodsReceipts;
}
