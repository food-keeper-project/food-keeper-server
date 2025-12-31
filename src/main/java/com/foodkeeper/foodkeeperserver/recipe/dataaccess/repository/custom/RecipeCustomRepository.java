package com.foodkeeper.foodkeeperserver.recipe.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.foodkeeper.foodkeeperserver.common.domain.SliceObject;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.entity.RecipeEntity;

public interface RecipeCustomRepository {

    SliceObject<RecipeEntity> findRecipes(Cursorable<Long> cursorable, String memberKey);
}
