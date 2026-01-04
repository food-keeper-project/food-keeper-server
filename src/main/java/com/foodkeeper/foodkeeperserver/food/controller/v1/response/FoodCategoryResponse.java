package com.foodkeeper.foodkeeperserver.food.controller.v1.response;

import com.foodkeeper.foodkeeperserver.food.domain.FoodCategory;

public record FoodCategoryResponse(Long id, String name) {
    public static FoodCategoryResponse from(FoodCategory foodCategory) {
        return new FoodCategoryResponse(foodCategory.id(), foodCategory.name());
    }
}
