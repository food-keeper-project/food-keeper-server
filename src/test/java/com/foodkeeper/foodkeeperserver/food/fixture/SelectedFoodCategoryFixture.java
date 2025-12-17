package com.foodkeeper.foodkeeperserver.food.fixture;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.SelectedFoodCategoryEntity;
import com.foodkeeper.foodkeeperserver.food.domain.SelectedFoodCategory;

public class SelectedFoodCategoryFixture {

    public static SelectedFoodCategory createSelectedCategory(Long foodId, Long categoryId) {
        return SelectedFoodCategory.create(foodId, categoryId);
    }

    public static SelectedFoodCategoryEntity createSelectedCategoryEntity(Long foodId, Long categoryId) {
        return SelectedFoodCategoryEntity.from(createSelectedCategory(foodId, categoryId));
    }

}
