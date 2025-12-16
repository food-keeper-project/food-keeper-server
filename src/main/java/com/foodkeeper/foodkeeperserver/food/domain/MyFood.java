package com.foodkeeper.foodkeeperserver.food.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record MyFood(
        Long id,
        String name,
        String imageUrl,
        StorageMethod storageMethod,
        LocalDate expiryDate,
        String memo,
        LocalDateTime createdAt,
        List<Long> categoryIds
) {
    public static MyFood of(Food food, List<Long> categoryIds) {
        return new MyFood(
                food.id(),
                food.name(),
                food.imageUrl(),
                food.storageMethod(),
                food.expiryDate(),
                food.memo(),
                food.createdAt(),
                categoryIds
        );
    }
}
