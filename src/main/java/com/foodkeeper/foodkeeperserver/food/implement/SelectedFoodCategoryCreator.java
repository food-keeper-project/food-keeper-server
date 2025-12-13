package com.foodkeeper.foodkeeperserver.food.implement;

import com.foodkeeper.foodkeeperserver.food.entity.FoodCategory;
import com.foodkeeper.foodkeeperserver.food.entity.SelectedFoodCategory;
import com.foodkeeper.foodkeeperserver.food.repository.SelectedFoodCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SelectedFoodCategoryCreator {

    private final SelectedFoodCategoryRepository selectedFoodCategoryRepository;

    public Long save(Long foodId, Long foodCategoryId) {
        SelectedFoodCategory selectedFoodCategory = SelectedFoodCategory.create(foodId, foodCategoryId);
        return selectedFoodCategoryRepository.save(selectedFoodCategory).getId();
    }
}
