package com.foodkeeper.foodkeeperserver.food.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.enums.EntityStatus;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodCategoryEntity;
import com.foodkeeper.foodkeeperserver.support.repository.QuerydslRepositorySupport;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.foodkeeper.foodkeeperserver.food.dataaccess.entity.QFoodCategoryEntity.foodCategoryEntity;
import static com.foodkeeper.foodkeeperserver.food.dataaccess.entity.QSelectedFoodCategoryEntity.selectedFoodCategoryEntity;

public class FoodCategoryCustomRepositoryImpl extends QuerydslRepositorySupport implements FoodCategoryCustomRepository {

    protected FoodCategoryCustomRepositoryImpl() {
        super(FoodCategoryEntity.class);
    }

    @Override
    public void deleteFoodCategories(String memberKey) {
        update(foodCategoryEntity)
                .set(foodCategoryEntity.status, EntityStatus.DELETED)
                .set(foodCategoryEntity.deletedAt, LocalDateTime.now())
                .where(foodCategoryEntity.memberKey.eq(memberKey), isNotDeleted())
                .execute();

        getEntityManager().clear();
    }

    @Override
    public List<FoodCategoryEntity> findAllByMemberKey(String memberKey) {
        return selectFrom(foodCategoryEntity)
                .where(foodCategoryEntity.memberKey.eq(memberKey), isNotDeleted())
                .fetch();
    }

    @Override
    public Optional<FoodCategoryEntity> findByIdAndMemberKey(Long id, String memberKey) {
        return Optional.ofNullable(
                selectFrom(foodCategoryEntity)
                        .where(foodCategoryEntity.id.eq(id), foodCategoryEntity.memberKey.eq(memberKey), isNotDeleted())
                        .fetchOne()
        );
    }

    @Override
    public List<FoodCategoryResult> findAllByIdIn(List<Long> foodIds) {
        return select(Projections.constructor(FoodCategoryResult.class,
                selectedFoodCategoryEntity.foodId,
                selectedFoodCategoryEntity.foodCategoryId,
                foodCategoryEntity.name,
                foodCategoryEntity.memberKey,
                foodCategoryEntity.createdAt))
                .from(foodCategoryEntity)
                .rightJoin(selectedFoodCategoryEntity)
                .on(selectedFoodCategoryEntity.foodCategoryId.eq(foodCategoryEntity.id))
                .where(selectedFoodCategoryEntity.foodId.in(foodIds), isNotDeleted())
                .fetch();
    }

    @Override
    public List<FoodCategoryResult> findAllById(Long id) {
        return select(Projections.constructor(FoodCategoryResult.class,
                selectedFoodCategoryEntity.foodId,
                selectedFoodCategoryEntity.foodCategoryId,
                foodCategoryEntity.name,
                foodCategoryEntity.memberKey,
                foodCategoryEntity.createdAt))
                .from(foodCategoryEntity)
                .rightJoin(selectedFoodCategoryEntity)
                .on(selectedFoodCategoryEntity.foodCategoryId.eq(foodCategoryEntity.id))
                .where(selectedFoodCategoryEntity.foodId.eq(id), isNotDeleted())
                .fetch();
    }

    private static BooleanExpression isNotDeleted() {
        return foodCategoryEntity.status.ne(EntityStatus.DELETED);
    }
}
