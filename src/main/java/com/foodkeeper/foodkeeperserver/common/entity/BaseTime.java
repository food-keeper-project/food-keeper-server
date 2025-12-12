package com.foodkeeper.foodkeeperserver.common.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTime {

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR")
    private EntityStatus status;

    public void delete() {
        status = EntityStatus.DELETED;
        deletedAt = LocalDateTime.now();
    }

    public boolean isDeleted() {
        return status == EntityStatus.DELETED;
    }
}
