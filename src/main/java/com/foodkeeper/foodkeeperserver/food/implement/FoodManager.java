package com.foodkeeper.foodkeeperserver.food.implement;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.FoodRepository;
import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodCursorFinder;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FoodManager {

    private final FoodRepository foodRepository;

    public Food register(Food food) {
        FoodEntity foodEntity = foodRepository.save(FoodEntity.from(food));
        return foodEntity.toDomain();
    }

    public List<Food> findFoodList(FoodCursorFinder foodFinder) {
        List<FoodEntity> foods = foodRepository.findFoodCursorList(foodFinder);
        return foods.stream()
                .map(FoodEntity::toDomain)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public Food findFood(Long id, String memberId) {
        return foodRepository.findByIdAndMemberId(id, memberId).orElseThrow(() -> new AppException(ErrorType.FOOD_DATA_NOT_FOUND))
                .toDomain();
    }

    // 사용자가 식재료 선택 -> foodId 값으로 이름 조회 -> 응답값으로 AI 요청
    public List<String> findFoodNames(List<Long> ids, String memberId) {
        return foodRepository.findNamesByIdAndMemberId(ids, memberId);
    }

    // 알림 설정 리스트 조회
    public List<Food> findImminentFoods(String memberId) {
        List<FoodEntity> foods = foodRepository.findImminentFoods(memberId);
        return foods.stream()
                .map(FoodEntity::toDomain)
                .toList();
    }

}
