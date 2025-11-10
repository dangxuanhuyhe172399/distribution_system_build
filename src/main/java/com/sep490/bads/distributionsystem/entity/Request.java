package com.sep490.bads.distributionsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sep490.bads.distributionsystem.entity.type.CommonStatus;
import com.sep490.bads.distributionsystem.entity.type.RequestStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;

import java.util.List;

@Entity
@Table(name = "Request", schema = "dbo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Request extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long id;

    @Column(name = "request_code", length = 50, unique = true)
    private String requestCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private SalesOrder order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_status", length = 100)
    private RequestStatus requestStatus;

    @Column(name = "request_type", length = 20, nullable = false)
    private String requestType;

    @Column(name = "reason", length = 255)
    private String reason;

    @Column(name = "reason_detail", length = 255)
    private String reasonDetail;

    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", updatable = false, nullable = false)
    private User createdBy;

    @OneToMany(mappedBy = "request", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<RequestDetail> requestDetails;
}
