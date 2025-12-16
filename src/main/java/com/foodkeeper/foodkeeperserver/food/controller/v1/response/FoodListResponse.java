package com.foodkeeper.foodkeeperserver.food.controller.v1.response;

import com.foodkeeper.foodkeeperserver.food.domain.MyFood;

import java.util.List;

public record FoodListResponse(List<MyFood> foodResponses, boolean hasNext) {
}
