package com.sep490.bads.distributionsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name = "ExportNote", schema = "dbo")
@NoArgsConstructor @AllArgsConstructor
public class ExportNote {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "export_note_id")
    private Long id;

    @Column(name = "export_date")
    private LocalDateTime exportDate;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @Column(name = "reason", length = 255)
    private String reason;

    @Column(name = "total_quantity")
    private Long totalQuantity;

    @Column(name = "status", length = 20)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "note", length = 255)
    private String note;
}
