package com.foodkeeper.foodkeeperserver.food.implement;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.FoodRepository;
import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodRegister;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FoodManager {

    private final FoodRepository foodRepository;
    private final SelectedFoodCategoryManager selectedFoodCategoryManager;

    @Transactional
    public Food register(Food food) {
        FoodEntity foodEntity = foodRepository.save(FoodEntity.from(food));
        return foodEntity.toDomain();
    }

    @Transactional
    public void updateFood(Food food, List<Long> categoryIds, String memberKey) {
        FoodEntity foodEntity = foodRepository.findByIdAndMemberKey(food.id(), memberKey).orElseThrow(() -> new AppException(ErrorType.FOOD_DATA_NOT_FOUND));
        foodEntity.update(food);
        if (categoryIds != null) {
            selectedFoodCategoryManager.update(food.id(), categoryIds);
        }
    }

    @Transactional
    public Food removeFood(Long id, String memberKey) {
        FoodEntity foodEntity = foodRepository.findByIdAndMemberKey(id, memberKey).orElseThrow(() -> new AppException(ErrorType.FOOD_DATA_NOT_FOUND));
        foodEntity.delete();
        selectedFoodCategoryManager.removeAllByFoodId(id);
        return foodEntity.toDomain();
    }


    public Food find(Long foodId) {
        return foodRepository.findById(foodId).orElseThrow(() -> new AppException(ErrorType.NOT_FOUND_DATA)).toDomain();
    }
}
