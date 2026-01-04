package com.foodkeeper.foodkeeperserver.food.business;

import com.foodkeeper.foodkeeperserver.food.domain.FoodCategory;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodCategoryRegister;
import com.foodkeeper.foodkeeperserver.food.implement.CategoryManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodCategoryService {

    private final CategoryManager categoryManager;

    public void registerFoodCategory(FoodCategoryRegister foodCategoryRegister) {
        categoryManager.addCategory(foodCategoryRegister.name(), foodCategoryRegister.memberKey());
    }

    public List<FoodCategory> findAllByMemberKey(String memberKey) {
        return categoryManager.findAllByMemberKey(memberKey);
    }

    public void updateCategory(Long id, String name, String memberKey) {
        categoryManager.updateCategory(id, name, memberKey);
    }

    public void removeCategory(Long id, String memberKey) {
        categoryManager.removeCategory(id, memberKey);
    }
}
