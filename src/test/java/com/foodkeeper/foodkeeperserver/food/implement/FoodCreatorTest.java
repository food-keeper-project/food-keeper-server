package com.foodkeeper.foodkeeperserver.food.implement;

import com.foodkeeper.foodkeeperserver.food.entity.Food;
import com.foodkeeper.foodkeeperserver.food.fixture.FoodFixture;
import com.foodkeeper.foodkeeperserver.food.repository.FoodRepository;
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
public class FoodCreatorTest {

    @InjectMocks
    private FoodCreator foodCreator;

    @Mock
    private FoodRepository foodRepository;

    @Test
    @DisplayName("식재료 저장 요청 시 리포지토리 호출 및 결과 반환")
    void save_SUCCESS() throws Exception {
        //given
        Food food = FoodFixture.createFood(1L);
        Food savedFood = FoodFixture.createFood(1L);

        given(foodRepository.save(any(Food.class))).willReturn(savedFood);
        //when
        Food resultFood = foodCreator.save(food);
        //then
        assertThat(resultFood).isEqualTo(savedFood);
    }
}
