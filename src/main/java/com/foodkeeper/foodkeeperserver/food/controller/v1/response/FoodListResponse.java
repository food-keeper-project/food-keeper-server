package com.foodkeeper.foodkeeperserver.food.controller.v1.response;

import java.util.List;

public record FoodListResponse(List<FoodResponse> foodResponseList) {
}
