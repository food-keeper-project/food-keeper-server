package com.foodkeeper.foodkeeperserver.recipe.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.foodkeeper.foodkeeperserver.common.domain.SliceObject;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.entity.RecipeEntity;

import java.util.List;
import java.util.Optional;

public interface RecipeCustomRepository {

    SliceObject<RecipeEntity> findRecipes(Cursorable<Long> cursorable, String memberKey);

    List<Long> deleteRecipes(String memberKey);

    long recipeCount(String memberKey);

    Optional<RecipeEntity> findByIdAndMemberKey(Long id, String memberKey);
}
