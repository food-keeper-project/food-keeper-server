package com.foodkeeper.foodkeeperserver.food.implement;

import com.foodkeeper.foodkeeperserver.food.entity.Food;
import com.foodkeeper.foodkeeperserver.food.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FoodCreator {

    private final FoodRepository foodRepository;

    public Food save(Food food){
        return foodRepository.save(food);
    }
}
