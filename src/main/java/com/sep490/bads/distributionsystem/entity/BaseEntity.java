package com.sep490.bads.distributionsystem.entity;

import com.sep490.bads.distributionsystem.utils.DateUtils;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
@SuperBuilder
public abstract class BaseEntity {

    @Column(name = "created_at")
    private Long createdAt;

    @Column(name = "updated_at")
    private Long updatedAt;

    @PrePersist
    protected void onCreate() {
        updatedAt = createdAt = DateUtils.getNowMillisAtUtc();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = DateUtils.getNowMillisAtUtc();
    }

}