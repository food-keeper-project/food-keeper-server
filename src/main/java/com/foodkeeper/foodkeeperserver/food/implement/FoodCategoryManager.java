package com.foodkeeper.foodkeeperserver.food.implement;

import com.foodkeeper.foodkeeperserver.common.utils.ListUtil;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodCategoryEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.FoodCategoryRepository;
import com.foodkeeper.foodkeeperserver.food.domain.FoodCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FoodCategoryManager {

    private final FoodCategoryRepository foodCategoryRepository;

    // 카테고리 먼저 조회
    @Transactional(readOnly = true)
    public List<FoodCategory> findAllByIds(List<Long> categoryIds) {
        List<FoodCategoryEntity> foodCategories = ListUtil.getOrElseThrowList(foodCategoryRepository.findAllById(categoryIds));
        return foodCategories.stream()
                .map(FoodCategoryEntity::toDomain)
                .toList();
    }

    @Transactional
    public void addCategory(String name, String memberId) {
        FoodCategory foodCategory = FoodCategory.create(name, memberId);
        foodCategoryRepository.save(FoodCategoryEntity.from(foodCategory));
    }

    @Transactional(readOnly = true)
    public List<FoodCategory> findAllByMemberId(String memberId) {
        List<FoodCategoryEntity> foodCategories = ListUtil.getOrElseThrowList(foodCategoryRepository.findAllByMemberId(memberId));
        return foodCategories.stream()
                .map(FoodCategoryEntity::toDomain)
                .toList();
    }

}
