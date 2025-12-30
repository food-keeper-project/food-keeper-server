package com.foodkeeper.foodkeeperserver.recipe.controller.v1.response;

import com.foodkeeper.foodkeeperserver.recipe.domain.Ingredient;

public record IngredientResponse(String name, String quantity) {

    public static IngredientResponse from(Ingredient ingredient) {
        return new IngredientResponse(ingredient.name(), ingredient.name());
    }
}
