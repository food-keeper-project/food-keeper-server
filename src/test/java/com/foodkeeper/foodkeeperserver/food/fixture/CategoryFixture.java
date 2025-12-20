package com.foodkeeper.foodkeeperserver.food.fixture;


import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodCategoryEntity;
import com.foodkeeper.foodkeeperserver.food.domain.FoodCategory;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

public class CategoryFixture {

    public static final String MEMBER_ID = "memberKey";

    public static List<FoodCategory> createCategory(List<Long> ids) {
        return ids.stream()
                .map(CategoryFixture::createCategory)
                .toList();
    }

    public static List<FoodCategoryEntity> createCategoryEntity(List<Long> ids) {
        return ids.stream()
                .map(CategoryFixture::createCategoryEntity)
                .toList();
    }

    // 단일 도메인 생성
    public static FoodCategory createCategory(Long id) {
        return new FoodCategory(id, "카테고리" + id, MEMBER_ID);
    }

    // 단일 엔티티 생성
    public static FoodCategoryEntity createCategoryEntity(Long id) {
        FoodCategoryEntity foodCategoryEntity = FoodCategoryEntity.from(createCategory(id));
        ReflectionTestUtils.setField(foodCategoryEntity, "id", id);
        return foodCategoryEntity;
    }
}
