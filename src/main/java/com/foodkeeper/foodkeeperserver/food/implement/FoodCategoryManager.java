package com.foodkeeper.foodkeeperserver.food.implement;

import com.foodkeeper.foodkeeperserver.common.utils.ListUtil;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodCategoryEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.FoodCategoryRepository;
import com.foodkeeper.foodkeeperserver.food.domain.DefaultFoodCategory;
import com.foodkeeper.foodkeeperserver.food.domain.FoodCategory;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FoodCategoryManager {

    private final FoodCategoryRepository foodCategoryRepository;

    @Transactional
    public void registerDefaultCategories(String memberKey) {
        Arrays.stream(DefaultFoodCategory.values()).forEach(category ->
                foodCategoryRepository.save(FoodCategoryEntity.builder()
                        .name(category.getValue())
                        .memberKey(memberKey)
                        .build()));
    }

    // 카테고리 먼저 조회
    @Transactional(readOnly = true)
    public List<FoodCategory> findAllByIds(List<Long> categoryIds) {
        List<FoodCategoryEntity> foodCategories = ListUtil.getOrElseThrowList(foodCategoryRepository.findAllById(categoryIds));
        return foodCategories.stream()
                .map(FoodCategoryEntity::toDomain)
                .toList();
    }

    @Transactional
    public void addCategory(String name, String memberKey) {
        FoodCategory foodCategory = FoodCategory.create(name, memberKey);
        foodCategoryRepository.save(FoodCategoryEntity.from(foodCategory));
    }

    @Transactional(readOnly = true)
    public List<FoodCategory> findAllByMemberKey(String memberKey) {
        List<FoodCategoryEntity> foodCategories = ListUtil.getOrElseThrowList(foodCategoryRepository.findAllByMemberKey(memberKey));
        return foodCategories.stream()
                .map(FoodCategoryEntity::toDomain)
                .toList();
    }

    @Transactional
    public void updateCategory(Long id, String name, String memberKey) {
        foodCategoryRepository.findByIdAndMemberKey(id, memberKey).orElseThrow(() -> new AppException(ErrorType.CATEGORY_DATA_NOT_FOUND)).update(name);
    }

    @Transactional
    public void removeCategory(Long id, String memberKey) {
        FoodCategoryEntity foodCategoryEntity = foodCategoryRepository.findByIdAndMemberKey(id, memberKey).orElseThrow(() -> new AppException(ErrorType.CATEGORY_DATA_NOT_FOUND));
        foodCategoryEntity.delete();
    }

}
