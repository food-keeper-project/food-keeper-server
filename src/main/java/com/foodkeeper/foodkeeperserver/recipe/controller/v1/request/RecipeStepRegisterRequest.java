package com.foodkeeper.foodkeeperserver.recipe.controller.v1.request;

import com.foodkeeper.foodkeeperserver.recipe.domain.RecipeStep;
import jakarta.validation.constraints.NotBlank;

public record RecipeStepRegisterRequest(@NotBlank String title, @NotBlank String content) {

    public RecipeStep toRecipeStep() {
        return new RecipeStep(title, content);
    }
}
