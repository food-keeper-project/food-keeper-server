package com.foodkeeper.foodkeeperserver.recipe.controller.v1.response;

import com.foodkeeper.foodkeeperserver.recipe.domain.Ingredient;
import com.foodkeeper.foodkeeperserver.recipe.domain.RecipeStep;

import java.util.List;

public record RecipeResponse(String menuName,
                             String description,
                             String totalTime,
                             List<Ingredient> ingredients,
                             List<RecipeStep> steps) {
}
