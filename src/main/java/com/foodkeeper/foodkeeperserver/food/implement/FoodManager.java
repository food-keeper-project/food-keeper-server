package com.foodkeeper.foodkeeperserver.food.implement;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.FoodRepository;
import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    public Food removeFood(Long id, String memberId) {
        FoodEntity foodEntity = foodRepository.findByIdAndMemberKey(id, memberId).orElseThrow(() -> new AppException(ErrorType.FOOD_DATA_NOT_FOUND));
        foodEntity.delete();
        selectedFoodCategoryManager.removeAllByFoodId(id);
        return foodEntity.toDomain();
    }


}
