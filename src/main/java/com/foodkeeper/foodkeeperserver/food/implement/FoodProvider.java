package com.foodkeeper.foodkeeperserver.food.implement;

import com.foodkeeper.foodkeeperserver.common.domain.SliceObject;
import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.domain.RegisteredFood;
import com.foodkeeper.foodkeeperserver.food.domain.SelectedFoodCategories;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FoodProvider {

    private final FoodCategoryManager foodCategoryManager;
    private final SelectedFoodCategoryManager selectedFoodCategoryManager;

    public SliceObject<RegisteredFood> findFoodList(SliceObject<Food> foods) {
        SelectedFoodCategories categories = new SelectedFoodCategories(selectedFoodCategoryManager.findByFoodIds(
                foods.content().stream().map(Food::id).toList()
        ));
        Map<Long, String> nameMap = foodCategoryManager.findNamesByIds(categories.getAllCategoryIds());
        return foods.map(food -> food.toRegisteredFood(categories.getCategoryNamesByFoodId(food.id(), nameMap)));
    }

    public RegisteredFood findFood(Food food) {
        SelectedFoodCategories categories = new SelectedFoodCategories(
                selectedFoodCategoryManager.findByFoodId(food.id())
        );
        Map<Long, String> nameMap = foodCategoryManager.findNamesByIds(categories.getAllCategoryIds());
        return food.toRegisteredFood(categories.getCategoryNamesByFoodId(food.id(), nameMap));
    }

    public List<RegisteredFood> findAllFoods(List<Food> foods) {
        SelectedFoodCategories categories = new SelectedFoodCategories(selectedFoodCategoryManager.findByFoodIds(
                foods.stream().map(Food::id).toList()
        ));
        Map<Long, String> nameMap = foodCategoryManager.findNamesByIds(categories.getAllCategoryIds());
        return foods.stream().map(food -> food.toRegisteredFood(categories.getCategoryNamesByFoodId(food.id(), nameMap))).toList();
    }


}
