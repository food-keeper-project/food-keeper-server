package com.foodkeeper.foodkeeperserver.food.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodCursorFinder;

import java.util.List;

public interface FoodRepositoryCustom {
    List<FoodEntity> findFoodCursorList(FoodCursorFinder foodFinder);

    List<String> findNamesByIdAndMemberId(List<Long> ids, String memberId);

    List<FoodEntity> findImminentFoods(String memberId);

}
