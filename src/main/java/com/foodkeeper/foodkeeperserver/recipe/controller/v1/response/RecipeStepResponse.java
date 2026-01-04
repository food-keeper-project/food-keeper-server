package com.foodkeeper.foodkeeperserver.recipe.controller.v1.response;

import com.foodkeeper.foodkeeperserver.recipe.domain.RecipeStep;

public record RecipeStepResponse(String title, String content) {

    public static RecipeStepResponse from(RecipeStep recipeStep) {
        return new RecipeStepResponse(recipeStep.title(), recipeStep.content());
    }
}