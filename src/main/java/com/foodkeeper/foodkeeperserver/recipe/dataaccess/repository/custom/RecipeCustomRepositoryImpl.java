package com.foodkeeper.foodkeeperserver.recipe.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.enums.EntityStatus;
import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.foodkeeper.foodkeeperserver.common.domain.SliceObject;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.entity.RecipeEntity;
import com.foodkeeper.foodkeeperserver.support.repository.QuerydslRepositorySupport;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.List;

import static com.foodkeeper.foodkeeperserver.recipe.dataaccess.entity.QRecipeEntity.recipeEntity;

public class RecipeCustomRepositoryImpl extends QuerydslRepositorySupport implements RecipeCustomRepository {

    protected RecipeCustomRepositoryImpl() {
        super(RecipeEntity.class);
    }

    @Override
    public SliceObject<RecipeEntity> findRecipes(Cursorable<Long> cursorable, String memberKey) {
        List<RecipeEntity> content = selectFrom(recipeEntity)
                .where(ltCursor(cursorable.cursor()), recipeEntity.memberKey.eq(memberKey))
                .where(recipeEntity.status.ne(EntityStatus.DELETED))
                .orderBy(recipeEntity.id.desc())
                .limit(cursorable.limit() + 1)
                .fetch();

        return new SliceObject<>(content, cursorable, hasNext(cursorable, content));
    }

    @Override
    public List<Long> deleteRecipes(String memberKey) {
        List<Long> recipeIds = select(recipeEntity.id)
                .from(recipeEntity)
                .where(recipeEntity.memberKey.eq(memberKey), recipeEntity.status.ne(EntityStatus.DELETED))
                .fetch();

        update(recipeEntity)
                .set(recipeEntity.status, EntityStatus.DELETED)
                .where(recipeEntity.memberKey.eq(memberKey), recipeEntity.status.ne(EntityStatus.DELETED))
                .execute();

        return recipeIds;
    }

    private static BooleanExpression ltCursor(Long cursor) {
        return cursor == null ? null : recipeEntity.id.lt(cursor);
    }
}
