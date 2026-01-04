package com.foodkeeper.foodkeeperserver.recipe.controller.v1.response;

import com.foodkeeper.foodkeeperserver.recipe.domain.RecipeIngredient;

public record IngredientResponse(String name, String quantity) {

    public static IngredientResponse from(RecipeIngredient ingredient) {
        return new IngredientResponse(ingredient.name(), ingredient.quantity());
    }
}
