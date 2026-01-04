package com.foodkeeper.foodkeeperserver.recipe.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.enums.EntityStatus;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.entity.RecipeStepEntity;
import com.foodkeeper.foodkeeperserver.support.repository.QuerydslRepositorySupport;

import java.util.List;

import static com.foodkeeper.foodkeeperserver.recipe.dataaccess.entity.QRecipeStepEntity.recipeStepEntity;

public class RecipeStepCustomRepositoryImpl extends QuerydslRepositorySupport implements RecipeStepCustomRepository {

    protected RecipeStepCustomRepositoryImpl() {
        super(RecipeStepEntity.class);
    }

    @Override
    public void deleteAllByRecipeIds(List<Long> recipeIds) {
        update(recipeStepEntity)
                .set(recipeStepEntity.status, EntityStatus.DELETED)
                .where(recipeStepEntity.recipeId.in(recipeIds))
                .execute();
    }
}
