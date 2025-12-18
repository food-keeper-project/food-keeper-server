package com.foodkeeper.foodkeeperserver.food.controller.v1.response;

import com.foodkeeper.foodkeeperserver.food.domain.RegisteredFood;

import java.util.List;

public record FoodListResponse(List<RegisteredFood> foodResponses, boolean hasNext) {
}
