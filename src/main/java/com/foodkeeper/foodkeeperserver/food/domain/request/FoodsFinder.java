package com.foodkeeper.foodkeeperserver.food.domain.request;

import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;

import java.time.LocalDateTime;

public record FoodsFinder(
        String memberKey,
        Long categoryId,
        LocalDateTime lastCreatedAt,
        Cursorable cursorable
) {
}
