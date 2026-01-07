package com.foodkeeper.foodkeeperserver.recipe.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.recipe.dataaccess.entity.RecipeIngredientEntity;

import java.util.List;

public interface RecipeIngredientCustomRepository {
    void deleteAllByRecipeIds(List<Long> recipeIds);

    List<RecipeIngredientEntity> findByRecipeId(Long recipeId);
}
