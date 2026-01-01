package com.foodkeeper.foodkeeperserver.recipe.dataaccess.repository.custom;

import java.util.List;

public interface RecipeStepCustomRepository {
    void deleteAllByRecipeIds(List<Long> recipeIds);
}
