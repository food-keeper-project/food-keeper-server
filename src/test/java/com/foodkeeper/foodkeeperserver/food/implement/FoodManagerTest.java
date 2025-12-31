package com.foodkeeper.foodkeeperserver.food.implement;

import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.foodkeeper.foodkeeperserver.common.domain.SliceObject;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.FoodRepository;
import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.fixture.FoodFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FoodManagerTest {

    @InjectMocks
    FoodManager foodManager;

    @Mock
    FoodRepository foodRepository;

    @Test
    @DisplayName("식재료 저장 요청 시 리포지토리 호출 및 결과 반환")
    void register_SUCCESS() {
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
    void findFoodList_SUCCESS() {
        //given
        Long categoryId = 1L;
        String memberKey = FoodFixture.MEMBER_KEY;
        long foodId = 1L;
        Cursorable<Long> cursorable = new Cursorable<>(foodId, 2);

        List<FoodEntity> foodEntities = List.of(
                FoodFixture.createFoodEntity(1L),
                FoodFixture.createFoodEntity(2L));
        SliceObject<FoodEntity> foodSlice = new SliceObject<>(foodEntities, cursorable, false);

        given(foodRepository.findFoodCursorList(cursorable, categoryId, memberKey)).willReturn(foodSlice);
        //when
        SliceObject<Food> results = foodManager.findFoodList(cursorable, categoryId, memberKey);
        //then
        assertThat(results.content()).hasSize(2);
        assertThat(results.content().getFirst().name()).isEqualTo(FoodFixture.NAME);
        assertThat(results.content().getFirst()).isInstanceOf(Food.class);
    }

    @Test
    @DisplayName("식재료 단일 조회 시 식재료 세부정보 반환")
    void findFoodById_SUCCESS() {
        //given
        Long foodId = FoodFixture.ID;
        String memberKey = FoodFixture.MEMBER_KEY;

        FoodEntity foodEntity = FoodFixture.createFoodEntity(foodId);
        given(foodRepository.findByIdAndMemberKey(foodId, memberKey)).willReturn(Optional.of(foodEntity));
        //when
        Food food = foodManager.findFood(foodId, memberKey);
        //then
        assertThat(food.name()).isEqualTo(FoodFixture.NAME);
        assertThat(food.memberKey()).isEqualTo(memberKey);
    }

    @Test
    @DisplayName("선택된 식재료들의 이름 조회 시 이름 리스트 반환")
    void findFoodNames_SUCCESS() {
        //given
        String memberKey = FoodFixture.MEMBER_KEY;
        FoodEntity entity1 = FoodFixture.createFoodEntity(1L);
        FoodEntity entity2 = FoodFixture.createFoodEntity(2L);
        given(foodRepository.findAllByMemberKey(memberKey)).willReturn(List.of(entity1, entity2));
        //when
        List<FoodEntity> foods = foodRepository.findAllByMemberKey(memberKey);
        //then
        assertThat(foods).hasSize(2);
        assertThat(foods.getFirst().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("식재료 삭제")
    void removeFood_SUCCESS() {
        //given
        Food food = FoodFixture.createFood(1L);
        //when
        foodManager.removeFood(food);
        //then
        ArgumentCaptor<FoodEntity> captor = ArgumentCaptor.forClass(FoodEntity.class);
        verify(foodRepository).delete(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo(food.name());
    }

    @Test
    @DisplayName("오늘 날짜를 기준으로 알림 날짜에 부합하는 foods 조회")
    void findFoodsToNotify_SUCCESS() throws Exception {
        //given
        LocalDate today = LocalDate.of(2025,12,26);
        List<FoodEntity> foods = List.of(mock(FoodEntity.class));
        given(foodRepository.findFoodsToNotify(today)).willReturn(foods);
        //when
        foodManager.findFoodsToNotify(today);
        //then
        verify(foodRepository).findFoodsToNotify(today);
    }
}
