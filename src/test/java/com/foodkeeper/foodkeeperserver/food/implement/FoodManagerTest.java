package com.foodkeeper.foodkeeperserver.food.implement;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.FoodRepository;
import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.fixture.FoodFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class FoodManagerTest {

    @InjectMocks
    private FoodManager foodManager;

    @Mock
    private FoodRepository foodRepository;

    @Test
    @DisplayName("식재료 저장 요청 시 리포지토리 호출 및 결과 반환")
    void register_SUCCESS() {
        //given
        Food food = FoodFixture.createFood();
        FoodEntity foodEntity = FoodFixture.createFoodEntity();

        given(foodRepository.save(any(FoodEntity.class))).willReturn(foodEntity);

        //when
        Food savedFood = foodManager.register(food);

        //then
        assertThat(savedFood.name()).isEqualTo(foodEntity.getName());
    }
}
