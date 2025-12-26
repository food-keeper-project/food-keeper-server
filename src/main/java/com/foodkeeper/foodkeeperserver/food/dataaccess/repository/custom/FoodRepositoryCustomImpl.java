package com.foodkeeper.foodkeeperserver.food.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.support.repository.QuerydslRepositorySupport;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static com.foodkeeper.foodkeeperserver.food.dataaccess.entity.QFoodEntity.foodEntity;
import static com.foodkeeper.foodkeeperserver.food.dataaccess.entity.QSelectedFoodCategoryEntity.selectedFoodCategoryEntity;

@Repository
public class FoodRepositoryCustomImpl extends QuerydslRepositorySupport implements FoodRepositoryCustom {

    protected FoodRepositoryCustomImpl() {
        super(FoodEntity.class);
    }

    // 카테고리 분류 조회
    @Override
    public List<FoodEntity> findFoodCursorList(Cursorable<LocalDateTime> cursorable, Long categoryId, String memberKey) {
        JPAQuery<FoodEntity> query = selectFrom(foodEntity);

        applyCategoryFilter(query, categoryId);

        return query
                .where(
                        foodEntity.memberKey.eq(memberKey),
                        foodEntity.createdAt.lt(cursorable.cursor())
                        // todo isDeleted == false 인 데이터만 조건 추가
                )
                .orderBy(foodEntity.createdAt.desc(), foodEntity.id.desc())
                .limit(cursorable.limit() + 1)
                .fetch();
    }

    @Override
    public List<FoodEntity> findAllByMemberKey(String memberKey) {

        return selectFrom(foodEntity)
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
        List<FoodEntity> foods = selectFrom(foodEntity)
                .where(foodEntity.memberKey.eq(memberKey))
                .fetch();

        LocalDate today = LocalDate.now();
        return foods.stream()
                .filter(food -> food.isImminent(today))
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
