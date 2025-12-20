package com.foodkeeper.foodkeeperserver.recipe.controller.v1.response;

import com.foodkeeper.foodkeeperserver.recipe.domain.Ingredient;

import java.util.List;

public record RecipeResponse(String menuName,
                             String totalTime,
                             List<Ingredient> ingredients,
                             List<String> steps) {
}
