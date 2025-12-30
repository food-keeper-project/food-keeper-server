package com.foodkeeper.foodkeeperserver.food.implement;

import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.foodkeeper.foodkeeperserver.common.domain.SliceObject;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.FoodRepository;
import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodRegister;
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
    private final SelectedFoodCategoryManager selectedFoodCategoryManager;

    @Transactional
    public Food registerFood(Food food) {
        FoodEntity foodEntity = foodRepository.save(FoodEntity.from(food));
        return foodEntity.toDomain();
    }

    @Transactional(readOnly = true)
    public SliceObject<Food> findFoodList(Cursorable<LocalDateTime> cursorable, Long categoryId, String memberKey) {
        return foodRepository.findFoodCursorList(cursorable, categoryId, memberKey)
                .map(FoodEntity::toDomain);
    }

    @Transactional(readOnly = true)
    public Food findFood(Long id, String memberKey) {
        return foodRepository.findByIdAndMemberKey(id, memberKey).orElseThrow(() -> new AppException(ErrorType.FOOD_DATA_NOT_FOUND))
                .toDomain();
    }

    // 1) 이름 정렬 2) 최신순
    @Transactional(readOnly = true)
    public List<Food> findAllByMemberKey(String memberKey) {
        return foodRepository.findAllByMemberKey(memberKey).stream()
                .map(FoodEntity::toDomain)
                .toList();
    }

    // 알림 설정 리스트 조회
    @Transactional(readOnly = true)
    public List<Food> findImminentFoods(String memberKey) {
        return foodRepository.findImminentFoods(memberKey).stream()
                .map(FoodEntity::toDomain)
                .toList();
    }

    @Transactional
    public void updateFood(Long foodId, FoodRegister register, String imageUrl) {
        FoodEntity foodEntity  = foodRepository.findById(foodId).orElseThrow(() -> new AppException(ErrorType.FOOD_DATA_NOT_FOUND));
        foodEntity.update(register, imageUrl);
        if(register.categoryIds() != null) {
            selectedFoodCategoryManager.update(foodId, register.categoryIds());
        }
    }

    @Transactional
    public Food removeFood(Long foodId, String memberId) {
        FoodEntity foodEntity = foodRepository.findByIdAndMemberKey(foodId, memberId).orElseThrow(() -> new AppException(ErrorType.FOOD_DATA_NOT_FOUND));
        foodEntity.delete();
        selectedFoodCategoryManager.removeAllByFoodId(foodId);
        return foodEntity.toDomain();
    }


    public Food find(Long foodId) {
        return foodRepository.findById(foodId).orElseThrow(() -> new AppException(ErrorType.NOT_FOUND_DATA)).toDomain();
    }
}
