package com.foodkeeper.foodkeeperserver.recipe.controller.v1.request;

import com.foodkeeper.foodkeeperserver.recipe.domain.NewRecipe;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RecipeRegisterRequest(@NotBlank String menuName,
                                    @NotNull String description,
                                    @NotNull Integer cookMinutes,
                                    @Valid List<IngredientRegisterRequest> ingredients,
                                    @Valid List<RecipeStepRegisterRequest> steps) {

    public NewRecipe toNewRecipe() {
        return NewRecipe.builder()
                .menuName(menuName)
                .description(description)
                .cookMinutes(cookMinutes)
                .ingredients(ingredients.stream().map(IngredientRegisterRequest::toIngredient).toList())
                .steps(steps.stream().map(RecipeStepRegisterRequest::toRecipeStep).toList())
                .build();
    }
}
