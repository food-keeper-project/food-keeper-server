package com.foodkeeper.foodkeeperserver.food.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.food.domain.FoodCategory;

import java.time.LocalDateTime;
import java.util.Map;

public record FoodCategoryResult(Long foodId,
                                 Long foodCategoryId,
                                 String foodCategoryName,
                                 String memberKey,
                                 LocalDateTime createdAt) {

    public Map.Entry<Long, FoodCategory> toEntry() {
        return Map.entry(foodId, new FoodCategory(foodCategoryId, foodCategoryName, memberKey, createdAt));
    }
}
