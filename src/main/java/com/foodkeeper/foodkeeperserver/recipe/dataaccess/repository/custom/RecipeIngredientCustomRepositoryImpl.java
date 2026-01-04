package com.foodkeeper.foodkeeperserver.recipe.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.enums.EntityStatus;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.entity.RecipeIngredientEntity;
import com.foodkeeper.foodkeeperserver.support.repository.QuerydslRepositorySupport;

import java.util.List;

import static com.foodkeeper.foodkeeperserver.recipe.dataaccess.entity.QRecipeIngredientEntity.recipeIngredientEntity;

public class RecipeIngredientCustomRepositoryImpl extends QuerydslRepositorySupport implements RecipeIngredientCustomRepository {

    protected RecipeIngredientCustomRepositoryImpl() {
        super(RecipeIngredientEntity.class);
    }

    @Override
    public void deleteAllByRecipeIds(List<Long> recipeIds) {
        update(recipeIngredientEntity)
                .set(recipeIngredientEntity.status, EntityStatus.DELETED)
                .where(recipeIngredientEntity.recipeId.in(recipeIds))
                .execute();
    }
}
