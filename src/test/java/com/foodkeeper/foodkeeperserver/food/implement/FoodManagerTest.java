package com.foodkeeper.foodkeeperserver.food.implement;

import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.foodkeeper.foodkeeperserver.common.domain.SliceObject;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.FoodRepository;
import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodsFinder;
import com.foodkeeper.foodkeeperserver.food.fixture.FoodFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

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
        Food food = FoodFixture.createFood();
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
        Long categoryId = 1L;
        String memberKey = FoodFixture.MEMBER_KEY;
        LocalDateTime lastCreatedAt = LocalDateTime.now();
        Cursorable cursorable = new Cursorable(1L,2);

        FoodEntity entity1 = FoodFixture.createFoodEntity(1L);
        FoodEntity entity2 = FoodFixture.createFoodEntity(2L);
        List<FoodEntity> foodEntities = List.of(entity1, entity2);

        given(foodRepository.findFoodCursorList(cursorable,categoryId,lastCreatedAt,memberKey)).willReturn(foodEntities);
        //when
        SliceObject<Food> results = foodManager.findFoodList(cursorable,categoryId,lastCreatedAt,memberKey);
        //then
        assertThat(results.getContent()).hasSize(2);
        assertThat(results.getContent().getFirst().name()).isEqualTo(FoodFixture.NAME);
        assertThat(results.getContent().get(0)).isInstanceOf(Food.class);
    }

    @Test
    @DisplayName("식재료 단일 조회 시 식재료 세부정보 반환")
    void findFoodById_SUCCESS() throws Exception {
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
    void findFoodNames_SUCCESS() throws Exception {
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
    void removeFood_SUCCESS() throws Exception {
        //given
        Long foodId = 1L;
        Food food = FoodFixture.createFood(foodId);
        //when
        foodManager.removeFood(food);
        //then
        ArgumentCaptor<FoodEntity> captor = ArgumentCaptor.forClass(FoodEntity.class);
        verify(foodRepository).delete(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo(food.name());
    }
}
