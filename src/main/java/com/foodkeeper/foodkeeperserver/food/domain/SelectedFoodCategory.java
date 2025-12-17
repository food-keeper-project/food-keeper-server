package com.foodkeeper.foodkeeperserver.food.domain;

import lombok.Builder;

@Builder
public record SelectedFoodCategory(
        Long id,
        Long foodId,
        Long foodCategoryId
) {
    public static SelectedFoodCategory create(Long foodId, Long foodCategoryId) {
        return SelectedFoodCategory.builder()
                .foodId(foodId)
                .foodCategoryId(foodCategoryId)
                .build();
    }
}
