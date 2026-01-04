package com.foodkeeper.foodkeeperserver.food.domain;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FoodCategory(
        Long id,
        String name,
        String memberKey,
        LocalDateTime createdAt
) {
    public static FoodCategory create(String name, String memberKey) {
        return FoodCategory.builder()
                .name(name)
                .memberKey(memberKey)
                .createdAt(LocalDateTime.now())
                .build();
    }

}
