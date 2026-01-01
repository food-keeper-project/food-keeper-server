package com.foodkeeper.foodkeeperserver.food.domain;
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
        LocalDateTime createdAt
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
                categoryNames,
                this.calculateRemainDay(LocalDate.now())
        );
    }

    public Food update(FoodRegister request, String imageUrl) {
        return Food.builder()
                .id(this.id)
                .name(request.name() != null ? request.name() : this.name)
                .storageMethod(request.storageMethod() != null ? request.storageMethod() : this.storageMethod)
                .expiryDate(request.expiryDate() != null ? request.expiryDate() : this.expiryDate)
                .expiryAlarm(request.expiryAlarm() != null ? request.expiryAlarm() : this.expiryAlarm)
                .memo(request.memo() != null ? request.memo() : this.memo)
                .imageUrl(imageUrl != null ? imageUrl : this.imageUrl)
                .build();
    }

    public long calculateRemainDay(LocalDate today) {
        return ChronoUnit.DAYS.between(today, this.expiryDate);
    }

}
