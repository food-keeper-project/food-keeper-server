package com.foodkeeper.foodkeeperserver.food.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FoodCategories {

    private final Map<Long, List<FoodCategory>> categories;

    private FoodCategories(Map<Long, List<FoodCategory>> categories) {
        this.categories = Collections.unmodifiableMap(new HashMap<>(categories));
    }

    public static FoodCategories from(List<SelectedFoodCategory> selectedFoodCategories, List<FoodCategory> foodCategories) {
        Map<Long, FoodCategory> categoryMap = foodCategories.stream()
                .collect(Collectors.toMap(FoodCategory::id, fc -> fc));

        Map<Long, List<FoodCategory>> grouped = selectedFoodCategories.stream()
                .collect(Collectors.groupingBy(
                        SelectedFoodCategory::foodId,
                        Collectors.mapping(
                                sfc -> categoryMap.get(sfc.foodCategoryId()),
                                Collectors.toUnmodifiableList()
                        )
                ));

        return new FoodCategories(grouped);
    }

    public List<FoodCategory> getCategories(Long foodId) {
        return categories.getOrDefault(foodId, List.of());
    }
}
