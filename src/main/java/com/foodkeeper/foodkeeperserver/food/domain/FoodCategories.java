package com.foodkeeper.foodkeeperserver.food.domain;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FoodCategories {

    private final Map<Long, List<String>> categoryNames;

    public FoodCategories(List<SelectedFoodCategory> selectedFoodCategories, List<FoodCategory> foodCategories) {
        Map<Long, String> categoryNameMap = foodCategories.stream()
                .collect(Collectors.toMap(FoodCategory::id, FoodCategory::name));

        this.categoryNames = selectedFoodCategories.stream()
                .collect(Collectors.groupingBy(
                        SelectedFoodCategory::foodId,
                        Collectors.mapping(
                                selectedFoodCategory -> categoryNameMap.get(selectedFoodCategory.foodCategoryId()),
                                Collectors.toList()
                        )
                ));
    }
    public List<String> getCategoryNames(Long foodId) {
        return categoryNames.getOrDefault(foodId, Collections.emptyList());
    }
}
