package com.foodkeeper.foodkeeperserver.food.domain;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.enums.EntityStatus;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodRegister;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Builder
public record Food(
        Long id,
        String name,
        String imageUrl,
        StorageMethod storageMethod,
        LocalDate expiryDate,
        Integer expiryAlarm,
        String memo,
        Integer selectedCategoryCount,
        String memberKey,
        LocalDateTime createdAt,
        EntityStatus status
) {

    public RegisteredFood toRegisteredFood(List<String> categoryNames) {
        return new RegisteredFood(
                this.id,
                this.name,
                this.imageUrl,
                this.storageMethod,
                this.expiryDate,
                this.expiryAlarm != null ? this.expiryAlarm : 2,
                this.memo,
                this.createdAt,
                this.status,
                categoryNames,
                this.calculateRemainDay(LocalDate.now())
        );
    }

    public void update(FoodRegister register, String newImageUrl) {
        if (request.name() != null) :this.name = request.name();
        if (request.storageMethod() != null) {
            this.storageMethod = request.storageMethod();
        }
        if (request.expiryDate() != null) {
            this.expiryDate = request.expiryDate();
        }
        if (request.expiryAlarm() != null) {
            this.expiryAlarm = request.expiryAlarm();
        }
        if (request.memo() != null) {
            this.memo = request.memo();
        }

        if (newImageUrl != null && !newImageUrl.isBlank()) {
            this.imageUrl = newImageUrl;
        }
    }

    private long calculateRemainDay(LocalDate today) {
        return ChronoUnit.DAYS.between(today, this.expiryDate);
    }

}
