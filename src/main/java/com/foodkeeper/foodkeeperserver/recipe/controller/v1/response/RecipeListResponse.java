package com.foodkeeper.foodkeeperserver.recipe.controller.v1.response;

import com.foodkeeper.foodkeeperserver.recipe.domain.Recipe;
import lombok.Builder;

@Builder
public record RecipeListResponse(String title,
                                 String description,
                                 Integer cookMinutes) {

    public static RecipeListResponse from(Recipe recipe) {
        return RecipeListResponse.builder()
                .title(recipe.menuName())
                .description(recipe.description())
                .cookMinutes(recipe.cookMinutes())
                .build();
    }
}
