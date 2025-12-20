package com.foodkeeper.foodkeeperserver.food.implement;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.FoodRepository;
import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FoodManager {

    private final FoodRepository foodRepository;

    public Food register(Food food) {
        FoodEntity foodEntity = foodRepository.save(FoodEntity.from(food));
        return foodEntity.toDomain();
    }

    public Food find(Long foodId) {
        return foodRepository.findById(foodId).orElseThrow(() -> new AppException(ErrorType.NOT_FOUND_DATA)).toDomain();
    }
}
