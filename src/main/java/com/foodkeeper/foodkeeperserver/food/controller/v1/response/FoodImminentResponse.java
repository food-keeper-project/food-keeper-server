package com.foodkeeper.foodkeeperserver.food.controller.v1.response;

import com.foodkeeper.foodkeeperserver.food.domain.response.RecipeFood;

import java.util.List;

public record FoodImminentResponse(List<RecipeFood> recipeFoods) {
}
