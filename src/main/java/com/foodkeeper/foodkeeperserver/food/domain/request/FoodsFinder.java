package com.foodkeeper.foodkeeperserver.food.domain.request;

import java.time.LocalDateTime;

public record FoodsFinder(
        String memberKey,
        Long categoryId,
        LocalDateTime lastCreatedAt,
        Long lastId,
        Integer limit
) {
    public FoodsFinder {
        if (limit == null) {
            limit = 10;
        }
        if (limit > 100) {
            limit = 100;
        }
    }
}
