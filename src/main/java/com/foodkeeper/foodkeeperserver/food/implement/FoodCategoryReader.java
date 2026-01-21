package com.foodkeeper.foodkeeperserver.food.implement;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodCategoryEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.SelectedFoodCategoryEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.FoodCategoryRepository;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.SelectedFoodCategoryRepository;
import com.foodkeeper.foodkeeperserver.food.domain.FoodCategories;
import com.foodkeeper.foodkeeperserver.food.domain.SelectedFoodCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FoodCategoryReader {

    private final FoodCategoryRepository foodCategoryRepository;
    private final SelectedFoodCategoryRepository selectedFoodCategoryRepository;

    public FoodCategories findNamesByFoodIds(List<Long> foodIds) {
        List<SelectedFoodCategory> categoryIds = selectedFoodCategoryRepository.findByFoodIdIn(foodIds).stream()
                .map(SelectedFoodCategoryEntity::toDomain).toList();
        List<FoodCategoryEntity> foodCategories = foodCategoryRepository.findAllByIdIn(categoryIds.stream()
                .map(SelectedFoodCategory::foodCategoryId).toList());
        return FoodCategories.from(categoryIds, foodCategories.stream().map(FoodCategoryEntity::toDomain).toList());
    }

    public FoodCategories findNamesFoodById(Long foodId) {
        List<SelectedFoodCategory> categoryIds = selectedFoodCategoryRepository.findByFoodId(foodId).stream()
                .map(SelectedFoodCategoryEntity::toDomain).toList();
        List<FoodCategoryEntity> foodCategories = foodCategoryRepository.findAllByIdIn(categoryIds.stream()
                .map(SelectedFoodCategory::foodCategoryId).toList());
        return FoodCategories.from(categoryIds, foodCategories.stream().map(FoodCategoryEntity::toDomain).toList());
    }


}
