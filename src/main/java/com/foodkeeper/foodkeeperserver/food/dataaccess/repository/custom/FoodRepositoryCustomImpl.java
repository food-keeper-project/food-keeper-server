package com.foodkeeper.foodkeeperserver.food.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.enums.EntityStatus;
import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.foodkeeper.foodkeeperserver.common.domain.SliceObject;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.support.repository.QuerydslRepositorySupport;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;

import java.util.List;

import static com.foodkeeper.foodkeeperserver.food.dataaccess.entity.QFoodEntity.foodEntity;
import static com.foodkeeper.foodkeeperserver.food.dataaccess.entity.QSelectedFoodCategoryEntity.selectedFoodCategoryEntity;

public class FoodRepositoryCustomImpl extends QuerydslRepositorySupport implements FoodRepositoryCustom {

    protected FoodRepositoryCustomImpl() {
        super(FoodEntity.class);
    }

    // 카테고리 분류 조회
    @Override
    public SliceObject<FoodEntity> findFoodCursorList(Cursorable<Long> cursorable,
                                                      Long categoryId,
                                                      String memberKey) {
        JPAQuery<FoodEntity> query = selectFrom(foodEntity);

        applyCategoryFilter(query, categoryId);

        List<FoodEntity> content = query
                .where(
                        foodEntity.memberKey.eq(memberKey),
                        foodEntity.status.ne(EntityStatus.DELETED),
                        ltCursor(cursorable.cursor())
                )
                .orderBy(foodEntity.id.desc(), foodEntity.createdAt.desc())
                .limit(cursorable.limit() + 1)
                .fetch();

        return new SliceObject<>(content, cursorable, hasNext(cursorable, content));
    }

    private static BooleanExpression ltCursor(Long cursor) {
        return cursor == null ? null : foodEntity.id.lt(cursor);
    }

    @Override
    public List<FoodEntity> findAllByMemberKey(String memberKey) {

        return selectFrom(foodEntity)
                .where(
                        foodEntity.memberKey.eq(memberKey),
                        foodEntity.status.ne(EntityStatus.DELETED)
                )
                .orderBy(
                        foodEntity.name.asc(),
                        foodEntity.createdAt.desc()
                )
                .fetch();
    }


    @Override
    public List<FoodEntity> findImminentFoods(String memberKey) {
        return selectFrom(foodEntity)
                .where(
                        foodEntity.memberKey.eq(memberKey),
                        foodEntity.status.ne(EntityStatus.DELETED))
                .orderBy(foodEntity.expiryDate.asc())
                .fetch();
    }

    private void applyCategoryFilter(JPAQuery<FoodEntity> query, Long categoryId) {
        if (categoryId != null) {
            query.join(selectedFoodCategoryEntity)
                    .on(foodEntity.id.eq(selectedFoodCategoryEntity.foodId))
                    .where(selectedFoodCategoryEntity.foodCategoryId.eq(categoryId));
        }
    }


}
