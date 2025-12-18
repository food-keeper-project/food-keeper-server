package com.foodkeeper.foodkeeperserver.food.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record RegisteredFood(
        Long id,
        String name,
        String imageUrl,
        StorageMethod storageMethod,
        LocalDate expiryDate,
        Integer expiryAlarm,
        String memo,
        LocalDateTime createdAt,
        List<Long> categoryIds
) {

    public static RegisteredFood of(Food food, List<Long> categoryIds) {
        return new RegisteredFood(
                food.id(),
                food.name(),
                food.imageUrl(),
                food.storageMethod(),
                food.expiryDate(),
                food.expiryAlarm(),
                food.memo(),
                food.createdAt(),
                categoryIds
        );
    }
}
