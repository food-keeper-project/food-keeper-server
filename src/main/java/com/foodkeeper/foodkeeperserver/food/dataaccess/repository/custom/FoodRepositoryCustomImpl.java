package com.foodkeeper.foodkeeperserver.food.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.enums.EntityStatus;
import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.foodkeeper.foodkeeperserver.common.domain.SliceObject;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.support.repository.QuerydslRepositorySupport;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;

import java.time.LocalDate;
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
                        foodEntity.id.lt(cursorable.cursor()),
                        foodEntity.status.ne(EntityStatus.DELETED)
                )
                .orderBy(foodEntity.id.desc())
                .limit(cursorable.limit() + 1)
                .fetch();

        return new SliceObject<>(content, cursorable, hasNext(cursorable, content));
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


    // 유통기한 임박한 식재료 조회
    @Override
    public List<FoodEntity> findImminentFoods(String memberKey) {
        LocalDate today = LocalDate.now();
        return selectFrom(foodEntity)
                .where(
                        foodEntity.memberKey.eq(memberKey),
                        foodEntity.status.ne(EntityStatus.DELETED),
                        isImminent(today))
                .orderBy(foodEntity.expiryDate.asc())
                .fetch();
    }

    // 카테고리 선택했을 시 필터링 조회
    private void applyCategoryFilter(JPAQuery<FoodEntity> query, Long categoryId) {
        if (categoryId != null) {
            query.join(selectedFoodCategoryEntity)
                    .on(foodEntity.id.eq(selectedFoodCategoryEntity.foodId))
                    .where(selectedFoodCategoryEntity.foodCategoryId.eq(categoryId));
        }
    }

    private BooleanExpression isImminent(LocalDate today) {
        // 유통기한 지난 날짜는 Pass
        BooleanExpression startPoint = foodEntity.expiryDate.goe(today);
        DateTemplate<LocalDate> endPoint = Expressions.dateTemplate(
                LocalDate.class,
                "DATE_ADD({0}, INTERVAL {1} DAY)",
                today,
                foodEntity.expiryAlarm
        );
        return startPoint.and(foodEntity.expiryDate.loe(endPoint));
    }


}
