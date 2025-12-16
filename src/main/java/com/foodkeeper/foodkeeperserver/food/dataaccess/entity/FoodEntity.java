package com.foodkeeper.foodkeeperserver.food.dataaccess.entity;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.BaseEntity;
import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.domain.StorageMethod;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FoodEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_id")
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "storage_method", length = 20, nullable = false)
    private StorageMethod storageMethod;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Column(name = "expiry_alarm", nullable = false)
    private Integer expiryAlarm;

    @Column(name = "memo", nullable = false)
    private String memo;

    @Column(name = "selected_category_count", nullable = false)
    private int selectedCategoryCount;

    @Column(name = "member_id", nullable = false)
    private String memberId;

    @Builder
    private FoodEntity(
            String name,
            String imageUrl,
            StorageMethod storageMethod,
            LocalDate expiryDate,
            Integer expiryAlarm,
            String memo,
            int selectedCategoryCount,
            String memberId) {
        this.name = name;
        this.imageUrl = (imageUrl != null) ? imageUrl : "";
        this.storageMethod = storageMethod;
        this.expiryDate = expiryDate;
        this.expiryAlarm = (expiryAlarm == null) ? 2 : expiryAlarm;
        this.memo = (memo != null) ? memo : "";
        this.selectedCategoryCount = selectedCategoryCount;
        this.memberId = memberId;
    }

    public static FoodEntity from(Food food) {
        return FoodEntity.builder()
                .name(food.name())
                .imageUrl(food.imageUrl())
                .storageMethod(food.storageMethod())
                .expiryDate(food.expiryDate())
                .expiryAlarm(food.expiryAlarm())
                .memo(food.memo())
                .selectedCategoryCount(food.selectedCategoryCount())
                .memberId(food.memberId())
                .build();
    }

    public Food toDomain() {
        return new Food(
                this.id,
                this.name,
                this.imageUrl,
                this.storageMethod,
                this.expiryDate,
                this.expiryAlarm,
                this.memo,
                this.selectedCategoryCount,
                this.memberId,
                this.getCreatedAt()
        );
    }


    public boolean isImminent(LocalDate today) {
        long remainDay =  ChronoUnit.DAYS.between(today, this.expiryDate);
        return remainDay >= 0 && remainDay <= this.expiryAlarm;
    }
}
