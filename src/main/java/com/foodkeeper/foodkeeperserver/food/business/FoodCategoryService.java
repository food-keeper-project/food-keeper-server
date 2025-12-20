package com.foodkeeper.foodkeeperserver.food.business;

import com.foodkeeper.foodkeeperserver.food.domain.FoodCategory;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodCategoryRegister;
import com.foodkeeper.foodkeeperserver.food.implement.FoodCategoryManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodCategoryService {

    private final FoodCategoryManager foodCategoryManager;

    public void registerFoodCategory(FoodCategoryRegister foodCategoryRegister) {
        foodCategoryManager.addCategory(foodCategoryRegister.name(), foodCategoryRegister.memberId());
    }

    public List<FoodCategory> findAllByMemberId(String memberId) {
        return foodCategoryManager.findAllByMemberId(memberId);
    }
}
