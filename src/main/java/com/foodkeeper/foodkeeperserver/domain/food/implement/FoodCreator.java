package com.foodkeeper.foodkeeperserver.domain.food.implement;

import com.foodkeeper.foodkeeperserver.domain.food.entity.Food;
import com.foodkeeper.foodkeeperserver.domain.food.repository.FoodRepository;
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
