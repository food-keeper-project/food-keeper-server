package com.foodkeeper.foodkeeperserver.food.fixture;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodCategoryEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

public class CategoryFixture {

    // 카테고리 Mock 객체
    public static List<FoodCategoryEntity> createCategory(List<Long> ids) {
        return ids.stream()
                .map(id -> {
                    FoodCategoryEntity foodCategoryEntity = FoodCategoryEntity.create("카테고리" +id,"memberId");
                    ReflectionTestUtils.setField(foodCategoryEntity,"id",id);
                    return foodCategoryEntity;
                })
                .toList();
    }

}
