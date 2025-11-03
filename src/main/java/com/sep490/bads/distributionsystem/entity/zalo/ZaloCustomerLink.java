package com.sep490.bads.distributionsystem.entity.zalo;

import com.sep490.bads.distributionsystem.entity.BaseEntity;
import com.sep490.bads.distributionsystem.entity.Customer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ZaloCustomerLink", schema = "dbo")
@Getter @Setter @NoArgsConstructor
public class ZaloCustomerLink extends BaseEntity {
    @Id
    @Column(name = "zalo_user_id", length = 64)
    private String zaloUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "follow_status", length = 20)
    private String followStatus = "unknown";

    @Column(name = "consent_at")
    private LocalDateTime consentAt;

}

