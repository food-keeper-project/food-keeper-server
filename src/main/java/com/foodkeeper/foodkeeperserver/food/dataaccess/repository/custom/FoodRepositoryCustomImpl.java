package com.foodkeeper.foodkeeperserver.food.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.QFoodEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.QSelectedFoodCategoryEntity;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodsFinder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Comparator;
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
    public List<FoodEntity> findFoodCursorList(FoodsFinder foodFinder) {
        JPAQuery<FoodEntity> query = queryFactory
                .selectFrom(foodEntity);

        applyCategoryFilter(query, foodFinder.categoryId());

        return query
                .where(
                        foodEntity.memberKey.eq(foodFinder.memberKey()),
                        foodEntity.createdAt.lt(foodFinder.lastCreatedAt())
                )
                .orderBy(foodEntity.createdAt.desc(), foodEntity.id.desc())
                .limit(foodFinder.limit() + 1)
                .fetch();
    }

    @Override
    public List<FoodEntity> findAllByMemberKey(String memberKey) {

        return queryFactory
                .select(foodEntity)
                .from(foodEntity)
                .where(
                        foodEntity.memberKey.eq(memberKey)
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
        List<FoodEntity> foods = queryFactory
                .selectFrom(foodEntity)
                .where(foodEntity.memberKey.eq(memberKey))
                .fetch();

        LocalDate today = LocalDate.now();
        return foods.stream()
                .filter(food -> {
                    return food.isImminent(today);
                })
                .sorted(Comparator.comparing(FoodEntity::getExpiryDate)) // 유통기한순
                .toList();
    }

    // 카테고리 선택했을 시 필터링 조회
    private void applyCategoryFilter(JPAQuery<FoodEntity> query, Long categoryId) {
        if (categoryId != null) {
            query.join(selectedFoodCategoryEntity)
                    .on(foodEntity.id.eq(selectedFoodCategoryEntity.foodId))
                    .where(selectedFoodCategoryEntity.foodCategoryId.eq(categoryId));
        }
    }


}
