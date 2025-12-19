package com.foodkeeper.foodkeeperserver.food.domain;

import lombok.Builder;

@Builder
public record FoodCategory(
        Long id,
        String name,
        String memberId
) {
}
