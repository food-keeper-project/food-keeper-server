package com.foodkeeper.foodkeeperserver.food.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.QFoodEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.QSelectedFoodCategoryEntity;
import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodCursorFinder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FoodRepositoryCustomImpl implements FoodRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QFoodEntity foodEntity = QFoodEntity.foodEntity;
    private final QSelectedFoodCategoryEntity selectedFoodCategoryEntity = QSelectedFoodCategoryEntity.selectedFoodCategoryEntity;

    // 날자 최신순 정렬- 기본 디폴트,
    // 카테고리 분류 조회
    @Override
    public List<FoodEntity> findFoodCursorList(FoodCursorFinder foodFinder) {
        JPAQuery<FoodEntity> query = queryFactory
                .selectFrom(foodEntity);

        applyCategoryFilter(query, foodFinder.categoryId());

        return query
                .where(
                        foodEntity.memberId.eq(foodFinder.memberId()),
                        cursorCondition(foodFinder.lastCreatedAt(), foodFinder.lastId())
                )
                .orderBy(foodEntity.createdAt.desc(),foodEntity.id.desc())
                .limit(foodFinder.limit() + 1)
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

    // 날짜 내림차순
    private BooleanExpression cursorCondition(LocalDateTime lastCreatedAt, Long lastId) {
        if (lastCreatedAt == null || lastId == null) {
            return null;
        }
        return foodEntity.createdAt.lt(lastCreatedAt)
                .or(foodEntity.createdAt.eq(lastCreatedAt)
                .and(foodEntity.id.lt(lastId)));
    }


}
