package com.foodkeeper.foodkeeperserver.food.domain.request;

import java.time.LocalDateTime;

public record FoodCursorFinder(
        String memberId,
        Long categoryId,
        LocalDateTime lastCreatedAt,
        Long lastId,
        Integer limit
) {
    public FoodCursorFinder {
        if (limit == null) {
            limit = 10;
        }
    }
}
