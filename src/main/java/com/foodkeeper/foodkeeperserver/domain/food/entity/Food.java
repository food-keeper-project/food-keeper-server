package com.foodkeeper.foodkeeperserver.domain.food.entity;

import com.foodkeeper.foodkeeperserver.common.entity.BaseTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "food")
public class Food extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_id")
    private Long id;

    @Column(name = "name",nullable = false, length = 50)
    private String name;

    @Column(name = "image_url",nullable = false)
    private String imageUrl;

    @Column(name = "memo",nullable = false)
    private String memo;

    @Enumerated(EnumType.STRING)
    @Column(name = "storage_method", length = 20, nullable = false)
    private StorageMethod storageMethod;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Column(name = "expiry_alert_days_before")
    private int expiryAlertDaysBefore; // 디폴트 D-2

    @Column(name = "selected_category_count", nullable = false)
    private int selectedCategoryCount = 0;

    @Column(name = "member_id", nullable = false)
    private String memberId;
}
