package com.sep490.bads.distributionsystem.entity.zalo;

import com.sep490.bads.distributionsystem.entity.BaseEntity;
import com.sep490.bads.distributionsystem.entity.type.ZaloEventLogStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ZaloEventLog", schema = "dbo")
@Getter @Setter @NoArgsConstructor
@AttributeOverrides({
        @AttributeOverride(
                name = "createdAt",
                column = @Column(name = "created_at", updatable = false, nullable = false)
        ),
        @AttributeOverride(
                name = "updatedAt",
                column = @Column(name = "updated_at", updatable = false, nullable = false)
        )
})
public class ZaloEventLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uniqueidentifier", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "event_id", length = 190, unique = true)
    private String eventId;

    @Column(name = "event_name", length = 190)
    private String eventName;

    @Column(name = "signature", length = 256)
    private String signature;


    @Column(name = "received_at", insertable = false, updatable = false)
    private LocalDateTime receivedAt;

    @Column(name = "processed_at", columnDefinition = "datetime")
    private LocalDateTime processedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 40, nullable = false)
    private ZaloEventLogStatus status = ZaloEventLogStatus.RECEIVED;

    @Lob @Column(name = "payload", columnDefinition = "NVARCHAR(MAX)")
    private String payload;
}
