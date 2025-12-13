package com.foodkeeper.foodkeeperserver.food.entity;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.BaseEntity;


import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Food extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_id")
    private Long id;

    @Column(name = "name",nullable = false, length = 50)
    private String name;

    @Column(name = "image_url",nullable = false)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "storage_method", length = 20, nullable = false)
    private StorageMethod storageMethod;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Column(name = "expiry_alert_days_before")
    private int expiryAlertDaysBefore;

    @Column(name = "memo",nullable = false)
    private String memo;

    @Column(name = "member_id", nullable = false)
    private String memberId;

    @Builder
    private Food(String name, String imageUrl, StorageMethod storageMethod, LocalDate expiryDate,
                Integer expiryAlertDaysBefore, String memo, int selectedCategoryCount, String memberId) {
        this.name = name;
        this.imageUrl = (imageUrl != null) ? imageUrl : "";
        this.storageMethod = storageMethod;
        this.expiryDate = expiryDate;
        this.expiryAlertDaysBefore = (expiryAlertDaysBefore == null) ? 2 : expiryAlertDaysBefore;
        this.memo = (memo != null) ? memo : "";
        this.memberId = memberId;
    }
}
