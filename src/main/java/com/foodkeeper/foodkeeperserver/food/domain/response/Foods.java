package com.foodkeeper.foodkeeperserver.food.domain.response;

import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.domain.RegisteredFood;
import com.foodkeeper.foodkeeperserver.food.domain.SelectedFoodCategories;

import java.util.List;

public record Foods(List<Food> foodList) {

    public List<Long> getFoodIds() {
        return foodList.stream()
                .map(Food::id)
                .toList();
    }

    public List<RegisteredFood> toRegisteredFoods(SelectedFoodCategories categories) {
        return foodList.stream()
                .map(food -> food.toFood(categories.getCategoryIdsByFoodId(food.id())))
                .toList();
    }
}
