package com.foodkeeper.foodkeeperserver.recipe.controller.v1.request;

import com.foodkeeper.foodkeeperserver.recipe.domain.Recipe;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RecipeRegisterRequest(@NotBlank String menuName,
                                    @NotNull String description,
                                    @NotNull Integer cookMinutes,
                                    @Valid List<IngredientRegisterRequest> ingredients,
                                    @Valid List<RecipeStepRegisterRequest> steps) {

    public Recipe toRecipe() {
        return Recipe.builder()
                .menuName(menuName)
                .description(description)
                .cookMinutes(cookMinutes)
                .ingredients(ingredients.stream().map(IngredientRegisterRequest::toIngredient).toList())
                .steps(steps.stream().map(RecipeStepRegisterRequest::toRecipeStep).toList())
                .build();
    }
}
