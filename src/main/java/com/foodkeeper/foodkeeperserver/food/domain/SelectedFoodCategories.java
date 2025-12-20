package com.foodkeeper.foodkeeperserver.food.domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SelectedFoodCategories {

    private final List<SelectedFoodCategory> selectedFoodCategories;
    private final Map<Long, List<Long>> categoriesGroupByFoodIds;

    public SelectedFoodCategories(List<SelectedFoodCategory> selectedFoodCategories) {
        this.selectedFoodCategories = selectedFoodCategories;
        this.categoriesGroupByFoodIds = groupByFoodIds();
    }

    // 식재료의 카테고리 List
    public List<Long> getCategoryIdsByFoodId(Long foodId) {
        return categoriesGroupByFoodIds.getOrDefault(foodId, List.of());
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
