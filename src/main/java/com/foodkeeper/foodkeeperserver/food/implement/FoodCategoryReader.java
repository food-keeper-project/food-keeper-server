package com.foodkeeper.foodkeeperserver.food.implement;

import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.FoodCategoryRepository;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.custom.FoodCategoryResult;
import com.foodkeeper.foodkeeperserver.food.domain.FoodCategories;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FoodCategoryReader {

    private final FoodCategoryRepository foodCategoryRepository;

    public FoodCategories findNamesByFoodIds(List<Long> foodIds) {
        return FoodCategories.from(foodCategoryRepository.findAllByIdIn(foodIds).stream()
                .map(FoodCategoryResult::toEntry).toList());
    }

    public FoodCategories findNamesFoodById(Long foodId) {
        return FoodCategories.from(foodCategoryRepository.findAllById(foodId).stream()
                .map(FoodCategoryResult::toEntry).toList());
    }
}
