package com.foodkeeper.foodkeeperserver.recipe.controller.v1.response;

import com.foodkeeper.foodkeeperserver.recipe.domain.Recipe;
import lombok.Builder;

@Builder
public record RecipeListResponse(Long id,
                                 String title,
                                 String description,
                                 Integer cookMinutes) {

    public static RecipeListResponse from(Recipe recipe) {
        return RecipeListResponse.builder()
                .id(recipe.id())
                .title(recipe.menuName())
                .description(recipe.description())
                .cookMinutes(recipe.cookMinutes())
                .build();
    }
}
