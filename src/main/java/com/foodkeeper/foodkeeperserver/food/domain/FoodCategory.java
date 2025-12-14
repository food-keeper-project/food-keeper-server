package com.foodkeeper.foodkeeperserver.food.domain;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodCategoryEntity;
import lombok.Builder;

@Builder
public record FoodCategory(
        Long id,
        String name,
        String memberId
) {
    public static FoodCategory create(String name, String memberId){
        return FoodCategory.builder()
                .name(name)
                .memberId(memberId)
                .build();
    }
}
