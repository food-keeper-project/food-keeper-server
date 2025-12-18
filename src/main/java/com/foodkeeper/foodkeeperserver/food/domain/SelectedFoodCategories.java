package com.foodkeeper.foodkeeperserver.food.domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record SelectedFoodCategories(List<SelectedFoodCategory> selectedFoodCategories) {

    // key : foodId, value : List<CategoryId> - 식재료에 category 매핑
    private Map<Long, List<Long>> getCategoryMap() {
        return selectedFoodCategories.stream()
                .collect(Collectors.groupingBy(
                        SelectedFoodCategory::foodId,
                        Collectors.mapping(SelectedFoodCategory::foodCategoryId, Collectors.toList())
                ));
    }
    // 식재료의 카테고리 List
    public List<Long> getCategoryIdsByFoodId(Long foodId) {
        return getCategoryMap().getOrDefault(foodId, List.of());
    }
}
