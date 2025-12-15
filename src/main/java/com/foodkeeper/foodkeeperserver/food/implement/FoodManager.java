package com.foodkeeper.foodkeeperserver.food.implement;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.FoodRepository;
import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodCursorFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FoodManager {

    private final FoodRepository foodRepository;

    public Food register(Food food) {
        FoodEntity foodEntity = foodRepository.save(FoodEntity.from(food));
        return foodEntity.toDomain();
    }

    public List<Food> findFoodList(FoodCursorFinder foodFinder, String memberId) {
        return foodRepository.findFoodCursorList(foodFinder,memberId);
    }

}
