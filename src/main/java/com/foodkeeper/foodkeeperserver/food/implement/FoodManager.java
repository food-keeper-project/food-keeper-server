package com.foodkeeper.foodkeeperserver.food.implement;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.FoodRepository;
import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodsFinder;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FoodManager {

    private final FoodRepository foodRepository;

    @Transactional
    public Food register(Food food) {
        FoodEntity foodEntity = foodRepository.save(FoodEntity.from(food));
        return foodEntity.toDomain();
    }

    @Transactional(readOnly = true)
    public List<Food> findFoodList(FoodsFinder foodFinder) {
        List<FoodEntity> foods = foodRepository.findFoodCursorList(foodFinder);
        return foods.stream()
                .map(FoodEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Food findFood(Long id, String memberId) {
        return foodRepository.findByIdAndMemberId(id, memberId).orElseThrow(() -> new AppException(ErrorType.FOOD_DATA_NOT_FOUND))
                .toDomain();
    }

    // 1) 이름 정렬 2) 최신순
    @Transactional(readOnly = true)
    public List<Food> findAllByMemberId(String memberId) {
        List<FoodEntity> foods = foodRepository.findAllByMemberId(memberId);
        return foods.stream()
                .map(FoodEntity::toDomain)
                .toList();
    }

    // 알림 설정 리스트 조회
    @Transactional(readOnly = true)
    public List<Food> findImminentFoods(String memberId) {
        List<FoodEntity> foods = foodRepository.findImminentFoods(memberId);
        return foods.stream()
                .map(FoodEntity::toDomain)
                .toList();
    }

    @Transactional
    public void removeFood(Food food) {
        foodRepository.delete(FoodEntity.from(food));
    }


    public Food find(Long foodId) {
        return foodRepository.findById(foodId).orElseThrow(() -> new AppException(ErrorType.NOT_FOUND_DATA)).toDomain();
    }
}
