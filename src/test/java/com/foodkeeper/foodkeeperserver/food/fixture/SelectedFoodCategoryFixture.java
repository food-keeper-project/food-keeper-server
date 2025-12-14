package com.foodkeeper.foodkeeperserver.food.fixture;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.SelectedFoodCategoryEntity;
import org.springframework.test.util.ReflectionTestUtils;

public class SelectedFoodCategoryFixture {

    public static SelectedFoodCategoryEntity createSelectedFoodCategory(Long id, Long foodId, Long foodCategoryId){
        SelectedFoodCategoryEntity selectedCategory = SelectedFoodCategoryEntity.create(foodId, foodCategoryId);
        ReflectionTestUtils.setField(selectedCategory, "id", id);
        return selectedCategory;
    }
}
