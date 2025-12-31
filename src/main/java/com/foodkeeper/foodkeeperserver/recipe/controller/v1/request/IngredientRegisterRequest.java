package com.foodkeeper.foodkeeperserver.recipe.controller.v1.request;

import com.foodkeeper.foodkeeperserver.recipe.domain.RecipeIngredient;
import jakarta.validation.constraints.NotBlank;

public record IngredientRegisterRequest(@NotBlank String name, @NotBlank String quantity) {

    public RecipeIngredient toIngredient() {
        return new RecipeIngredient(name, quantity);
    }
}
