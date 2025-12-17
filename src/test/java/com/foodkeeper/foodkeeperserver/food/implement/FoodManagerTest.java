package com.foodkeeper.foodkeeperserver.food.implement;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.FoodRepository;
import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodCursorFinder;
import com.foodkeeper.foodkeeperserver.food.fixture.FoodFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class FoodManagerTest {

    @InjectMocks
    FoodManager foodManager;

    @Mock
    FoodRepository foodRepository;

    @Test
    @DisplayName("식재료 저장 요청 시 리포지토리 호출 및 결과 반환")
    void register_SUCCESS() throws Exception {
        //given
        Food food = FoodFixture.createFood(1L);
        FoodEntity foodEntity = FoodFixture.createFoodEntity(1L);

        given(foodRepository.save(any(FoodEntity.class))).willReturn(foodEntity);
        //when
        Food savedFood = foodManager.register(food);
        //then
        assertThat(savedFood.name()).isEqualTo(foodEntity.getName());
    }

    @Test
    @DisplayName("식재료 커서 요청 시 리포지토리 호출 및 리스트 결과 반환")
    void findFoodList_SUCCESS() throws Exception {
        //given
        FoodCursorFinder finder = FoodFixture.createFirstPageFinder();

        FoodEntity entity1 = FoodFixture.createFoodEntity(1L);
        FoodEntity entity2 = FoodFixture.createFoodEntity(2L);
        List<FoodEntity> foodEntities = List.of(entity1, entity2);

        given(foodRepository.findFoodCursorList(finder)).willReturn(foodEntities);
        //when
        List<Food> results = foodManager.findFoodList(finder);
        //then
        assertThat(results).hasSize(2);
        assertThat(results.getFirst().name()).isEqualTo(FoodFixture.NAME);
        assertThat(results.get(0)).isInstanceOf(Food.class);
    }

    @Test
    @DisplayName("식재료 단일 조회 시 식재료 세부정보 반환")
    void findFoodById_SUCCESS() throws Exception {
        //given
        Long foodId = FoodFixture.ID;
        String memberId = FoodFixture.MEMBER_ID;

        FoodEntity foodEntity = FoodFixture.createFoodEntity(foodId);
        given(foodRepository.findByIdAndMemberId(foodId, memberId)).willReturn(Optional.of(foodEntity));
        //when
        Food food = foodManager.findFood(foodId, memberId);
        //then
        assertThat(food.name()).isEqualTo(FoodFixture.NAME);
        assertThat(food.memberId()).isEqualTo(memberId);
    }

    @Test
    @DisplayName("선택된 식재료들의 이름 조회 시 이름 리스트 반환")
    void findFoodNames_SUCCESS() throws Exception {
        //given
        String memberId = FoodFixture.MEMBER_ID;
        FoodEntity entity1 = FoodFixture.createFoodEntity(1L);
        FoodEntity entity2 = FoodFixture.createFoodEntity(2L);
        given(foodRepository.findAllByMemberId(memberId)).willReturn(List.of(entity1, entity2));
        //when
        List<FoodEntity> foods = foodRepository.findAllByMemberId(memberId);
        //then
        assertThat(foods).hasSize(2);
        assertThat(foods.getFirst().getId()).isEqualTo(1L);
    }

}
