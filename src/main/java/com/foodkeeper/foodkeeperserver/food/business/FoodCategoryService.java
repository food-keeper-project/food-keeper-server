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
        foodCategoryManager.addCategory(foodCategoryRegister.name(), foodCategoryRegister.memberKey());
    }

    public List<FoodCategory> findAllByMemberKey(String memberKey) {
        return foodCategoryManager.findAllByMemberKey(memberKey);
    }

    public void updateCategory(Long id, String name, String memberKey) {
        foodCategoryManager.updateCategory(id, name, memberKey);
    }

    public void removeCategory(Long id, String memberKey) {
        foodCategoryManager.removeCategory(id, memberKey);
    }
}
