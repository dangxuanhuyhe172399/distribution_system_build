package com.sep490.bads.distributionsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ExportNote")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExportNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exportNoteId;

    @Column(name = "export_date")
    private LocalDateTime exportDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @Column(length = 255)
    private String reason;

    @Column(name = "total_quantity")
    private Integer totalQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdByUser;

    @Column(length = 255)
    private String note;
}
