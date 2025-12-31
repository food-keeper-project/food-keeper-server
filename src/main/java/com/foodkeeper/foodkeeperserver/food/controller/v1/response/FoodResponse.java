package com.foodkeeper.foodkeeperserver.food.controller.v1.response;

import com.foodkeeper.foodkeeperserver.food.domain.RegisteredFood;
import com.foodkeeper.foodkeeperserver.food.domain.StorageMethod;

import java.time.LocalDateTime;
import java.util.List;

public record FoodResponse(
        Long id,
        String name,
        String imageUrl,
        StorageMethod storageMethod,
        LocalDateTime expiryDate,
        String memo,
        LocalDateTime createdAt,
        List<Long> categoryIds
) {
    public static FoodResponse toFoodResponse(RegisteredFood food) {
        return new FoodResponse(
                food.id(),
                food.name(),
                food.imageUrl(),
                food.storageMethod(),
                food.expiryDate().atStartOfDay(),
                food.memo(),
                food.createdAt(),
                food.categoryIds()
        );
    }
}
