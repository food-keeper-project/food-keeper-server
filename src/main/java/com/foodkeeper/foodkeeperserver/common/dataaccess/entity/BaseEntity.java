package com.foodkeeper.foodkeeperserver.common.dataaccess.entity;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.enums.EntityStatus;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deletedAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EntityStatus status;

    public BaseEntity() {
        this.status = EntityStatus.ACTIVE;
    }

    public void delete() {
        status = EntityStatus.DELETED;
        deletedAt = LocalDateTime.now();
    }

    public boolean isDeleted() {
        return status == EntityStatus.DELETED;
    }

    public void update() {
        updatedAt = LocalDateTime.now();
    }
}