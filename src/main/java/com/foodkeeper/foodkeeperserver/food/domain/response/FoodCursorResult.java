package com.foodkeeper.foodkeeperserver.food.domain.response;

import com.foodkeeper.foodkeeperserver.food.domain.MyFood;

import java.util.List;

public record FoodCursorResult(List<MyFood> foods, boolean hasNext) {
}
