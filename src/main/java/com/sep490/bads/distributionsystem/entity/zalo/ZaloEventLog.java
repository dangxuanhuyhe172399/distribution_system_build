package com.sep490.bads.distributionsystem.entity.zalo;

import com.sep490.bads.distributionsystem.entity.BaseEntity;

import com.sep490.bads.distributionsystem.entity.type.ZaloEventLogStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity @Table(name = "ZaloEventLog")
@Getter @Setter @NoArgsConstructor
public class ZaloEventLog extends BaseEntity {
    @Id @Column(columnDefinition = "uniqueidentifier")
    private UUID id = UUID.randomUUID();

    @Column(name = "event_id", length = 190, unique = true)
    private String eventId;

    @Column(name = "event_name", length = 190)
    private String eventName;

    @Column(name = "signature", length = 256)
    private String signature;


    @Column(name = "received_at", columnDefinition = "datetime")
    private LocalDateTime receivedAt;

    @Column(name = "processed_at", columnDefinition = "datetime")
    private LocalDateTime processedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 40, nullable = false)
    private ZaloEventLogStatus status = ZaloEventLogStatus.RECEIVED;

    @Lob @Column(name = "payload", columnDefinition = "NVARCHAR(MAX)")
    private String payload;
}
