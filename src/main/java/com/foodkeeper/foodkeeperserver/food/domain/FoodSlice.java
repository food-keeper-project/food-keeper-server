package com.foodkeeper.foodkeeperserver.food.domain;

import com.foodkeeper.foodkeeperserver.food.domain.response.FoodCursorResult;

import java.util.ArrayList;
import java.util.List;

public record FoodSlice(Foods foods, boolean hasNext) {

    public static FoodSlice from(List<Food> foods, int limit) {
        boolean hasNext = false;
        List<Food> foodList = new ArrayList<>(foods);

        if (foodList.size() > limit) {
            hasNext = true;
            foodList.remove(limit);
        }
        return new FoodSlice(new Foods(foodList), hasNext);
    }

    public FoodCursorResult toResult(SelectedFoodCategories categories) {
        return new FoodCursorResult(foods.toRegisteredFoods(categories), hasNext);
    }
}
