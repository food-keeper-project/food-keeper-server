package com.foodkeeper.foodkeeperserver.food.controller.v1.response;

import com.foodkeeper.foodkeeperserver.food.domain.RegisteredFood;
import com.foodkeeper.foodkeeperserver.food.domain.StorageMethod;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record FoodResponse(
        Long id,
        String name,
        String imageUrl,
        StorageMethod storageMethod,
        LocalDate expiryDate,
        Integer expiryAlarm,
        String memo,
        LocalDateTime createdAt,
        List<String> categoryNames,
        Long remainDays
) {
    public static FoodResponse toFoodResponse(RegisteredFood food) {
        return new FoodResponse(
                food.id(),
                food.name(),
                food.imageUrl(),
                food.storageMethod(),
                food.expiryDate(),
                food.expiryAlarm(),
                food.memo(),
                food.createdAt(),
                food.categoryNames(),
                food.remainDays()
        );
    }

    public static List<FoodResponse> toFoodListResponse(List<RegisteredFood> foods) {
        return foods.stream()
                .map(FoodResponse::toFoodResponse)
                .toList();
    }

}
