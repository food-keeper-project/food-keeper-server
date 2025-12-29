package com.foodkeeper.foodkeeperserver.food.domain;

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
        LocalDateTime createdAt
) {

    public RegisteredFood toRegisteredFood(List<String> categoryNames) {
        return new RegisteredFood(
                this.id,
                this.name,
                this.imageUrl,
                this.storageMethod,
                this.expiryDate,
                this.expiryAlarm,
                this.memo,
                this.createdAt,
                categoryNames,
                this.calculateRemainDay(LocalDate.now())
        );
    }

    private long calculateRemainDay(LocalDate today) {
        return ChronoUnit.DAYS.between(today, this.expiryDate);
    }

}
