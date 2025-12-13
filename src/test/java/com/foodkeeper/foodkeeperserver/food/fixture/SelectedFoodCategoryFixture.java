package com.foodkeeper.foodkeeperserver.food.fixture;

import com.foodkeeper.foodkeeperserver.food.entity.SelectedFoodCategory;
import org.springframework.test.util.ReflectionTestUtils;

public class SelectedFoodCategoryFixture {

    public static SelectedFoodCategory createSelectedFoodCategory(Long id,Long foodId,Long foodCategoryId){
        SelectedFoodCategory selectedCategory = SelectedFoodCategory.create(foodId, foodCategoryId);
        ReflectionTestUtils.setField(selectedCategory, "id", id);
        return selectedCategory;
    }
}
