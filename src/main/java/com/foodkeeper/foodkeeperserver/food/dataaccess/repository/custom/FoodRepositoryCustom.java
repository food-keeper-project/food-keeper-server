package com.foodkeeper.foodkeeperserver.food.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodsFinder;

import java.util.List;

public interface FoodRepositoryCustom {
    List<FoodEntity> findFoodCursorList(FoodsFinder foodFinder);

    List<FoodEntity> findAllByMemberKey(String memberKey);

    List<FoodEntity> findImminentFoods(String memberKey);

}
