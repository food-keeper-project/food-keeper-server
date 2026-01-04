package com.foodkeeper.foodkeeperserver.food.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.enums.EntityStatus;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodCategoryEntity;
import com.foodkeeper.foodkeeperserver.support.repository.QuerydslRepositorySupport;

import static com.foodkeeper.foodkeeperserver.food.dataaccess.entity.QFoodCategoryEntity.foodCategoryEntity;

public class FoodCategoryCustomRepositoryImpl extends QuerydslRepositorySupport implements FoodCategoryCustomRepository {

    protected FoodCategoryCustomRepositoryImpl() {
        super(FoodCategoryEntity.class);
    }

    @Override
    public void deleteFoodCategories(String memberKey) {
        update(foodCategoryEntity)
                .set(foodCategoryEntity.status, EntityStatus.DELETED)
                .where(foodCategoryEntity.memberKey.eq(memberKey), foodCategoryEntity.status.ne(EntityStatus.DELETED))
                .execute();
    }
}
