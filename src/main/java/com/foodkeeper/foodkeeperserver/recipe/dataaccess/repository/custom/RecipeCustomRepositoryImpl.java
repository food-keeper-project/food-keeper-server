package com.foodkeeper.foodkeeperserver.recipe.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.enums.EntityStatus;
import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.foodkeeper.foodkeeperserver.common.domain.SliceObject;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.entity.RecipeEntity;
import com.foodkeeper.foodkeeperserver.support.repository.QuerydslRepositorySupport;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.foodkeeper.foodkeeperserver.recipe.dataaccess.entity.QRecipeEntity.recipeEntity;

public class RecipeCustomRepositoryImpl extends QuerydslRepositorySupport implements RecipeCustomRepository {

    protected RecipeCustomRepositoryImpl() {
        super(RecipeEntity.class);
    }

    @Override
    public SliceObject<RecipeEntity> findRecipes(Cursorable<Long> cursorable, String memberKey) {
        List<RecipeEntity> content = selectFrom(recipeEntity)
                .where(ltCursor(cursorable.cursor()), eqMember(memberKey))
                .where(isNotDeleted())
                .orderBy(recipeEntity.id.desc())
                .limit(cursorable.limit() + 1)
                .fetch();

        return new SliceObject<>(content, cursorable, hasNext(cursorable, content));
    }

    @Override
    public List<Long> deleteRecipes(String memberKey) {
        List<Long> recipeIds = select(recipeEntity.id)
                .from(recipeEntity)
                .where(eqMember(memberKey), isNotDeleted())
                .fetch();

        update(recipeEntity)
                .set(recipeEntity.status, EntityStatus.DELETED)
                .set(recipeEntity.deletedAt, LocalDateTime.now())
                .where(eqMember(memberKey), isNotDeleted())
                .execute();

        getEntityManager().clear();

        return recipeIds;
    }

    @Override
    public long recipeCount(String memberKey) {
        Long count = select(recipeEntity.id.count())
                .from(recipeEntity)
                .where(eqMember(memberKey), isNotDeleted())
                .fetchOne();
        return count != null ? count : 0L;
    }

    @Override
    public Optional<RecipeEntity> findByIdAndMemberKey(Long id, String memberKey) {
        return Optional.ofNullable(
                selectFrom(recipeEntity)
                        .where(recipeEntity.id.eq(id), recipeEntity.memberKey.eq(memberKey), isNotDeleted())
                        .fetchOne()
        );
    }

    private static BooleanExpression eqMember(String memberKey) {
        return recipeEntity.memberKey.eq(memberKey);
    }

    private static BooleanExpression isNotDeleted() {
        return recipeEntity.status.ne(EntityStatus.DELETED);
    }

    private static BooleanExpression ltCursor(Long cursor) {
        return cursor == null ? null : recipeEntity.id.lt(cursor);
    }
}
