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
@Table(name = "food")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FoodEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private StorageMethod storageMethod;

    @Column(nullable = false)
    private LocalDate expiryDate;

    @Column(nullable = false)
    private Integer expiryAlarm;

    @Column(nullable = false)
    private String memo;

    @Column(nullable = false)
    private int selectedCategoryCount;

    @Column(nullable = false)
    private String memberKey;

    @Builder
    private FoodEntity(
            String name,
            String imageUrl,
            StorageMethod storageMethod,
            LocalDate expiryDate,
            Integer expiryAlarm,
            String memo,
            int selectedCategoryCount,
            String memberKey) {
        this.name = name;
        this.imageUrl = (imageUrl != null) ? imageUrl : "";
        this.storageMethod = storageMethod;
        this.expiryDate = expiryDate;
        this.expiryAlarm = (expiryAlarm == null) ? 2 : expiryAlarm;
        this.memo = (memo != null) ? memo : "";
        this.selectedCategoryCount = selectedCategoryCount;
        this.memberKey = memberKey;
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
                .memberKey(food.memberKey())
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
                this.memberKey,
                this.getCreatedAt()
        );
    }

}
