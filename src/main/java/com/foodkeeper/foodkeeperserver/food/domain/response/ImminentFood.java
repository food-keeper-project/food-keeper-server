package com.foodkeeper.foodkeeperserver.food.domain.response;

import com.foodkeeper.foodkeeperserver.food.domain.Food;

import java.time.LocalDate;

public record ImminentFood(Long id, String name, long remainDay) {

    public static ImminentFood of(Food food) {
        long remainDay = food.calculateRemainDay(LocalDate.now());
        return new ImminentFood(
                food.id(),
                food.name(),
                remainDay
        );
    }
}
