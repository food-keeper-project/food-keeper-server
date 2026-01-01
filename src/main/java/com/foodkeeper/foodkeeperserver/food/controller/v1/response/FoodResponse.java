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
        Integer expiryAlarm,
        String memo,
        LocalDateTime createdAt,
        List<FoodCategoryResponse> categories,
        Long remainDays
) {
    public static FoodResponse from(RegisteredFood food) {
        return new FoodResponse(
                food.id(),
                food.name(),
                food.imageUrl(),
                food.storageMethod(),
                food.expiryDate().atStartOfDay(),
                food.expiryAlarm(),
                food.memo(),
                food.createdAt(),
                food.categories().stream().map(FoodCategoryResponse::from).toList(),
                food.remainDays()
        );
    }

    public static List<FoodResponse> toFoodListResponse(List<RegisteredFood> foods) {
        return foods.stream()
                .map(FoodResponse::from)
                .toList();
    }

}
