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

    public void save(Long foodId, Long foodCategoryId) {
        SelectedFoodCategory selectedFoodCategory = SelectedFoodCategory.builder()
                .foodId(foodId)
                .foodCategoryId(foodCategoryId)
                .build();
        selectedFoodCategoryRepository.save(selectedFoodCategory);
    }
}
