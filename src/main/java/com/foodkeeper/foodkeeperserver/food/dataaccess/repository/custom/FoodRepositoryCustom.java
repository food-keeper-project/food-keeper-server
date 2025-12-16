package com.foodkeeper.foodkeeperserver.food.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodCursorFinder;

import java.util.List;

public interface FoodRepositoryCustom {
    List<FoodEntity> findFoodCursorList(FoodCursorFinder foodFinder);
    List<FoodEntity> findImminentFoods(String memberId);

}
