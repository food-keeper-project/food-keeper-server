package com.foodkeeper.foodkeeperserver.food.implement;

import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.foodkeeper.foodkeeperserver.common.domain.SliceObject;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.FoodRepository;
import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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
    public SliceObject<Food> findFoodList(Cursorable cursorable, Long categoryId, LocalDateTime lastCreatedAt, String memberKey) {
        List<FoodEntity> entities = foodRepository.findFoodCursorList(cursorable, categoryId, lastCreatedAt, memberKey);
        List<Food> foods = entities.stream().map(FoodEntity::toDomain).toList();
        return new SliceObject<>(foods, cursorable);
    }

    @Transactional(readOnly = true)
    public Food findFood(Long id, String memberKey) {
        return foodRepository.findByIdAndMemberKey(id, memberKey).orElseThrow(() -> new AppException(ErrorType.FOOD_DATA_NOT_FOUND))
                .toDomain();
    }

    // 1) 이름 정렬 2) 최신순
    @Transactional(readOnly = true)
    public List<Food> findAllByMemberKey(String memberKey) {
        List<FoodEntity> foods = foodRepository.findAllByMemberKey(memberKey);
        return foods.stream()
                .map(FoodEntity::toDomain)
                .toList();
    }

    // 알림 설정 리스트 조회
    @Transactional(readOnly = true)
    public List<Food> findImminentFoods(String memberKey) {
        List<FoodEntity> foods = foodRepository.findImminentFoods(memberKey);
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
