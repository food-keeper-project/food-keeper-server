package com.foodkeeper.foodkeeperserver.food.dataaccess.entity;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.BaseEntity;
import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.domain.StorageMethod;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodRegister;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


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
    private Integer expiryAlarmDays;

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
            Integer expiryAlarmDays,
            String memo,
            int selectedCategoryCount,
            String memberKey) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.storageMethod = storageMethod;
        this.expiryDate = expiryDate;
        this.expiryAlarmDays = expiryAlarmDays;
        this.memo = memo;
        this.selectedCategoryCount = selectedCategoryCount;
        this.memberKey = memberKey;
    }

    public static FoodEntity from(FoodRegister food, String imageUrl, String memberKey) {
        return FoodEntity.builder()
                .name(food.name())
                .imageUrl(imageUrl)
                .storageMethod(food.storageMethod())
                .expiryDate(food.expiryDate())
                .expiryAlarmDays(food.expiryAlarmDays())
                .memo(food.memo())
                .selectedCategoryCount(food.categoryIds().size())
                .memberKey(memberKey)
                .build();
    }

    public Food toDomain() {
        return new Food(
                this.id,
                this.name,
                this.imageUrl,
                this.storageMethod,
                this.expiryDate,
                this.expiryAlarmDays,
                this.memo,
                this.selectedCategoryCount,
                this.memberKey,
                this.getCreatedAt()
        );
    }

    public void update(Food food) {
        this.name = food.name();
        this.storageMethod = food.storageMethod();
        this.imageUrl = food.imageUrl();
        this.expiryDate = food.expiryDate();
        this.expiryAlarmDays = food.expiryAlarmDays();
        this.memo = food.memo();
    }

    public boolean isImminent(LocalDate today) {
        if (expiryDate.isBefore(today)) return false;
        LocalDate alarmLimitDate = today.plusDays(this.expiryAlarmDays);
        return !expiryDate.isAfter(alarmLimitDate);
    }

    public boolean isNotificationDay(LocalDate today) {
        LocalDate alarmDay = this.expiryDate.minusDays(this.expiryAlarmDays);
        return alarmDay.isEqual(today);
    }


}
