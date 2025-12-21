package com.foodkeeper.foodkeeperserver.food.domain;

import java.util.List;

public record Foods(List<Food> foodList) {

    public List<Long> getFoodIds() {
        return foodList.stream()
                .map(Food::id)
                .toList();
    }

    public List<RegisteredFood> toRegisteredFoods(SelectedFoodCategories categories) {
        return foodList.stream()
                .map(food -> food.toRegisteredFood(categories.getCategoryIdsByFoodId(food.id())))
                .toList();
    }
}
