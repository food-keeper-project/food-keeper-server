package com.foodkeeper.foodkeeperserver.food.fixture;

import com.foodkeeper.foodkeeperserver.food.dto.request.FoodRegisterRequest;
import com.foodkeeper.foodkeeperserver.food.entity.Food;
import com.foodkeeper.foodkeeperserver.food.entity.StorageMethod;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;

public class FoodFixture {

    // 식재료 요청 Mock 객체
    public static FoodRegisterRequest createFoodRegisterRequest(List<Long> categoryIds) {
        return new FoodRegisterRequest(
                "우유",
                categoryIds,
                StorageMethod.FROZEN,
                LocalDate.now().plusDays(1),
                3,
                "우유 마시쪙"
        );
    }

    // 식재료 Mock 엔티티
    public static Food createFood(Long id) {
        Food food = Food.builder().name("우유").build();
        ReflectionTestUtils.setField(food, "id", id);
        return food;
    }
}
