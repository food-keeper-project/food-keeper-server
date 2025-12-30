package com.foodkeeper.foodkeeperserver.recipe.domain;

import java.util.List;

public record Recipe(String menuName,
                     String description,
                     String totalTime,
                     List<Ingredient> ingredients,
                     List<RecipeStep> steps) {
}
