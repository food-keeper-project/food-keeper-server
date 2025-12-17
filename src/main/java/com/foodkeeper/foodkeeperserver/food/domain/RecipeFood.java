package com.foodkeeper.foodkeeperserver.food.domain;

import java.time.LocalDate;

public record RecipeFood(Long id, String name, long remainDay) {

    public static RecipeFood of(Food food) {
        long remainDay = food.calculateRemainDay(LocalDate.now());
        return new RecipeFood(
                food.id(),
                food.name(),
                remainDay
        );
    }
}
