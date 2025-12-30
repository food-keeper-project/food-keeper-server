package com.foodkeeper.foodkeeperserver.recipe.controller.v1.response;

import com.foodkeeper.foodkeeperserver.recipe.domain.Recipe;
import lombok.Builder;

import java.util.List;

@Builder
public record RecipeResponse(String menuName,
                             String description,
                             Integer cookMinutes,
                             List<IngredientResponse> ingredients,
                             List<RecipeStepResponse> steps) {

    public static RecipeResponse from(Recipe recipe) {
        return RecipeResponse.builder()
                .menuName(recipe.menuName())
                .description(recipe.description())
                .cookMinutes(recipe.cookMinutes())
                .ingredients(recipe.ingredients().stream().map(IngredientResponse::from).toList())
                .steps(recipe.steps().stream().map(RecipeStepResponse::from).toList())
                .build();
    }
}
