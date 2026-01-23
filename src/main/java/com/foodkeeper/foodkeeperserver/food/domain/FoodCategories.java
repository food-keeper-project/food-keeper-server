package com.foodkeeper.foodkeeperserver.food.domain;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

public class FoodCategories {

    private final Map<Long, List<FoodCategory>> categories;

    private FoodCategories(Map<Long, List<FoodCategory>> categories) {
        this.categories = Map.copyOf(categories);
    }

    public static FoodCategories from(List<Map.Entry<Long, FoodCategory>> categoriesByFoodId) {
        return new FoodCategories(categoriesByFoodId.stream()
                .collect(groupingBy(
                        Map.Entry::getKey,
                        mapping(Map.Entry::getValue, toList())))
        );
    }

    public List<FoodCategory> getCategories(Long foodId) {
        return categories.getOrDefault(foodId, List.of());
    }
}
