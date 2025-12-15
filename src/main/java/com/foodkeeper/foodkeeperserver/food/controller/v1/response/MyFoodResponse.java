package com.foodkeeper.foodkeeperserver.food.controller.v1.response;

import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.domain.StorageMethod;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record MyFoodResponse(
        Long id,
        String name,
        String imageUrl,
        StorageMethod storageMethod,
        LocalDate expiryDate,
        String memo,
        LocalDateTime createdAt,
        List<Long> categoryIds
) {
    public static MyFoodResponse toFoodResponse(Food food, List<Long> categoryIds) {
        return new MyFoodResponse(
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
