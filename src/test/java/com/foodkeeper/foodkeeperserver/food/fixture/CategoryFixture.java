package com.foodkeeper.foodkeeperserver.food.fixture;

import com.foodkeeper.foodkeeperserver.food.dto.request.FoodRegisterRequest;
import com.foodkeeper.foodkeeperserver.food.entity.Food;
import com.foodkeeper.foodkeeperserver.food.entity.FoodCategory;
import com.foodkeeper.foodkeeperserver.food.entity.StorageMethod;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class CategoryFixture {

    // 카테고리 Mock 객체
    public static List<FoodCategory> createCategory(List<Long> ids) {
        return ids.stream()
                .map(id -> {
                    FoodCategory foodCategory = FoodCategory.create("카테고리" +id,"memberId");
                    ReflectionTestUtils.setField(foodCategory,"id",id);
                    return foodCategory;
                })
                .toList();
    }

}
