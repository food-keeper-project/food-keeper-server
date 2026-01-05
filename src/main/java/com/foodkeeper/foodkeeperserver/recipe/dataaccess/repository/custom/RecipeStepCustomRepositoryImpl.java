package com.foodkeeper.foodkeeperserver.recipe.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.enums.EntityStatus;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.entity.RecipeStepEntity;
import com.foodkeeper.foodkeeperserver.support.repository.QuerydslRepositorySupport;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.time.LocalDateTime;
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
                .set(recipeStepEntity.deletedAt, LocalDateTime.now())
                .where(recipeStepEntity.recipeId.in(recipeIds), isNotDeleted())
                .execute();
    }

    @Override
    public List<RecipeStepEntity> findByRecipeId(Long recipeId) {
        return selectFrom(recipeStepEntity)
                .where(recipeStepEntity.recipeId.eq(recipeId), isNotDeleted())
                .fetch();
    }

    private static BooleanExpression isNotDeleted() {
        return recipeStepEntity.status.ne(EntityStatus.DELETED);
    }
}
