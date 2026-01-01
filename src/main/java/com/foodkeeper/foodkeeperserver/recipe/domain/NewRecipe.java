package com.foodkeeper.foodkeeperserver.recipe.domain;

import lombok.Builder;

import java.util.List;

@Builder
public record NewRecipe(String menuName,
                        String description,
                        Integer cookMinutes,
                        List<RecipeIngredient> ingredients,
                        List<RecipeStep> steps) {

    public NewRecipe {
        if (ingredients == null) {
            ingredients = List.of();
        }

        if (steps == null) {
            steps = List.of();
        }
    }
}
