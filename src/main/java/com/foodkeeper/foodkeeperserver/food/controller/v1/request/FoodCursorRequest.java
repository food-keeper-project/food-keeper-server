package com.foodkeeper.foodkeeperserver.food.controller.v1.request;

import com.foodkeeper.foodkeeperserver.food.domain.request.FoodCursorFinder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record FoodCursorRequest(
        Long categoryId,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastCreatedAt,
        Long lastId,
        Integer limit
) {
    public static FoodCursorFinder toFinder(FoodCursorRequest request, String memberId){
        return new FoodCursorFinder(
                memberId,
                request.categoryId(),
                request.lastCreatedAt,
                request.lastId,
                request.limit
        );
    }
}
