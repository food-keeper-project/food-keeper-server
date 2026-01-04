package com.foodkeeper.foodkeeperserver.food.domain;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FoodCategories {

    private final Map<Long, List<FoodCategory>> categories;

    public FoodCategories(List<SelectedFoodCategory> selectedFoodCategories, List<FoodCategory> foodCategories) {
        Map<Long, FoodCategory> categoryMap = foodCategories.stream()
                .collect(Collectors.toMap(FoodCategory::id, foodCategory -> foodCategory));

        this.categories = selectedFoodCategories.stream()
                .collect(Collectors.groupingBy(
                        SelectedFoodCategory::foodId,
                        Collectors.mapping(
                                selectedFoodCategory -> categoryMap.get(selectedFoodCategory.foodCategoryId()),
                                Collectors.toList()
                        )
                ));
    }
    public List<FoodCategory> getCategories(Long foodId) {
        return categories.getOrDefault(foodId, Collections.emptyList());
    }
}
