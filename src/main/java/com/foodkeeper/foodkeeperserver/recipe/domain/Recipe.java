package com.foodkeeper.foodkeeperserver.recipe.domain;

import lombok.Builder;

import java.util.List;

@Builder
public record Recipe(Long id,
                     String menuName,
                     String description,
                     Integer cookMinutes,
                     List<RecipeIngredient> ingredients,
                     List<RecipeStep> steps) {
}