package com.foodkeeper.foodkeeperserver.recipe.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.recipe.dataaccess.entity.RecipeStepEntity;

import java.util.List;

public interface RecipeStepCustomRepository {
    void deleteAllByRecipeIds(List<Long> recipeIds);

    List<RecipeStepEntity> findByRecipeId(Long recipeId);
}
