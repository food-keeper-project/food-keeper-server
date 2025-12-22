package com.foodkeeper.foodkeeperserver.food.domain;

import lombok.Builder;

@Builder
public record FoodCategory(
        Long id,
        String name,
        String memberKey
) {
    public static FoodCategory create(String name, String memberKey) {
        return FoodCategory.builder()
                .name(name)
                .memberKey(memberKey)
                .build();
    }
}
