package com.foodkeeper.foodkeeperserver.food.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodCursorFinder;

import java.util.List;

public interface FoodRepositoryCustom {
    List<Food> findFoodCursorList(FoodCursorFinder foodFinder, String memberId);
}
