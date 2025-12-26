package com.foodkeeper.foodkeeperserver.food.domain.response;

import com.foodkeeper.foodkeeperserver.food.domain.RegisteredFood;

import java.util.List;

public record FoodCursorResult(List<RegisteredFood> foods, boolean hasNext) {
}
