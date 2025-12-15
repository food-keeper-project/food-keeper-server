package com.foodkeeper.foodkeeperserver.food.controller.v1.response;

import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.domain.StorageMethod;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record MyFoodResponse(
        Long id,
        String name,
        String imageUrl,
        StorageMethod storageMethod,
        LocalDate expiryDate,
        String memo,
        LocalDateTime createdAt
) {
    public static MyFoodResponse toFoodResponse(Food food) {
        return new MyFoodResponse(
                food.id(),
                food.name(),
                food.imageUrl(),
                food.storageMethod(),
                food.expiryDate(),
                food.memo(),
                food.createdAt()
        );
    }
}
