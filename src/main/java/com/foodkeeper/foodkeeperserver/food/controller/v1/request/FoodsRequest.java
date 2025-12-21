package com.foodkeeper.foodkeeperserver.food.controller.v1.request;

import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodsFinder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record FoodsRequest(
        Long categoryId,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastCreatedAt
) {
    public static FoodsFinder toFinder(FoodsRequest request, Cursorable cursorable, String memberKey) {
        return new FoodsFinder(
                memberKey,
                request.categoryId,
                request.lastCreatedAt,
                cursorable
        );
    }
}
