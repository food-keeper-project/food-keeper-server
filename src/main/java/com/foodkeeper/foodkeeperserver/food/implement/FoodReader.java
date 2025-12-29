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

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FoodReader {

    private final FoodRepository foodRepository;

    public SliceObject<Food> findFoodList(Cursorable<Long> cursorable, Long categoryId, String memberKey) {
        return foodRepository.findFoodCursorList(cursorable, categoryId, memberKey)
                .map(FoodEntity::toDomain);
    }

    public Food findFood(Long id, String memberKey) {
        return foodRepository.findByIdAndMemberKey(id, memberKey).orElseThrow(() -> new AppException(ErrorType.FOOD_DATA_NOT_FOUND))
                .toDomain();
    }

    // 1) 이름 정렬 2) 최신순
    public List<Food> findAll(String memberKey) {
        return foodRepository.findAllByMemberKey(memberKey).stream()
                .map(FoodEntity::toDomain)
                .toList();
    }

    // 알림 설정 리스트 조회
    public List<Food> findImminentFoods(String memberKey) {
        return foodRepository.findImminentFoods(memberKey).stream()
                .map(FoodEntity::toDomain)
                .toList();
    }

    public Food find(Long foodId) {
        return foodRepository.findById(foodId).orElseThrow(() -> new AppException(ErrorType.NOT_FOUND_DATA)).toDomain();
    }
}
