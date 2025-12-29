package com.foodkeeper.foodkeeperserver.food.domain;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SelectedFoodCategories {

    private final List<SelectedFoodCategory> selectedFoodCategories;
    private final Map<Long, List<Long>> categoriesGroupByFoodIds;

    public SelectedFoodCategories(List<SelectedFoodCategory> selectedFoodCategories) {
        this.selectedFoodCategories = selectedFoodCategories;
        this.categoriesGroupByFoodIds = groupByFoodIds();
    }

    public List<Long> getAllCategoryIds() {
        return selectedFoodCategories.stream()
                .map(SelectedFoodCategory::foodCategoryId)
                .toList();
    }

    public List<String> getCategoryNamesByFoodId(Long foodId, Map<Long, String> nameMap) {
        List<Long> categoryIds = categoriesGroupByFoodIds.getOrDefault(foodId, Collections.emptyList());
        return categoryIds.stream()
                .map(nameMap::get)
                .toList();
    }


    // key : foodId, value : List<CategoryId> - 식재료에 category 매핑
    private Map<Long, List<Long>> groupByFoodIds() {
        return selectedFoodCategories.stream()
                .collect(Collectors.groupingBy(
                        SelectedFoodCategory::foodId,
                        Collectors.mapping(SelectedFoodCategory::foodCategoryId, Collectors.toList())
                ));
    }
}
