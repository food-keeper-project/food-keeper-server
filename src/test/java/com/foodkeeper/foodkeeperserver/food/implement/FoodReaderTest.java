package com.foodkeeper.foodkeeperserver.food.implement;

import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.foodkeeper.foodkeeperserver.common.domain.SliceObject;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.FoodRepository;
import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.domain.FoodCategories;
import com.foodkeeper.foodkeeperserver.food.domain.RegisteredFood;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class FoodReaderTest {

    @InjectMocks
    FoodReader foodReader;

    @Mock
    FoodRepository foodRepository;

    @Mock
    FoodCategoryReader foodCategoryReader;

    @Test
    @DisplayName("식재료 커서 리스트 조회 시 카테고리 정보와 매핑되어 RegisteredFood 반환")
    void findFoodList_SUCCESS() {
        // given
        Long categoryId = 1L;
        String memberKey = FoodFixture.MEMBER_KEY;
        Cursorable<Long> cursorable = new Cursorable<>(10L, 2);

        List<FoodEntity> foodEntities = List.of(
                FoodFixture.createFoodEntity(1L),
                FoodFixture.createFoodEntity(2L)
        );
        SliceObject<FoodEntity> foodSlice = new SliceObject<>(foodEntities, cursorable, false);

        FoodCategories mockFoodCategories = mock(FoodCategories.class);
        given(mockFoodCategories.getCategoryNames(anyLong())).willReturn(List.of("유제품", "냉동"));

        given(foodRepository.findFoodCursorList(cursorable, categoryId, memberKey)).willReturn(foodSlice);
        given(foodCategoryReader.findNamesByFoodIds(anyList())).willReturn(mockFoodCategories);

        // when
        SliceObject<RegisteredFood> result = foodReader.findFoodList(cursorable, categoryId, memberKey);

        // then
        assertThat(result.content()).hasSize(2);
        assertThat(result.content().getFirst()).isInstanceOf(RegisteredFood.class);
        assertThat(result.content().getFirst().categoryNames()).contains("유제품");
    }

    @Test
    @DisplayName("식재료 단일 조회 시 카테고리 정보를 포함하여 반환")
    void findFood_SUCCESS() {
        // given
        Long foodId = 1L;
        String memberKey = FoodFixture.MEMBER_KEY;
        FoodEntity foodEntity = FoodFixture.createFoodEntity(foodId);

        FoodCategories mockFoodCategories = mock(FoodCategories.class);
        given(mockFoodCategories.getCategoryNames(foodId)).willReturn(List.of("채소"));

        given(foodRepository.findByIdAndMemberKey(foodId, memberKey)).willReturn(Optional.of(foodEntity));
        given(foodCategoryReader.findNamesFoodById(foodId)).willReturn(mockFoodCategories);

        // when
        RegisteredFood result = foodReader.findFood(foodId, memberKey);

        // then
        assertThat(result.name()).isEqualTo(FoodFixture.NAME);
        assertThat(result.categoryNames()).contains("채소");
    }

    @Test
    @DisplayName("전체 식재료 조회(findAll) 시 RegisteredFood 리스트 반환")
    void findAll_SUCCESS() {
        // given
        String memberKey = FoodFixture.MEMBER_KEY;
        List<FoodEntity> foodEntities = List.of(
                FoodFixture.createFoodEntity(1L),
                FoodFixture.createFoodEntity(2L)
        );

        FoodCategories mockFoodCategories = mock(FoodCategories.class);
        given(mockFoodCategories.getCategoryNames(anyLong())).willReturn(List.of("기타"));

        given(foodRepository.findAllByMemberKey(memberKey)).willReturn(foodEntities);
        given(foodCategoryReader.findNamesByFoodIds(anyList())).willReturn(mockFoodCategories);

        // when
        List<RegisteredFood> results = foodReader.findAll(memberKey);

        // then
        assertThat(results).hasSize(2);
        assertThat(results.getFirst()).isInstanceOf(RegisteredFood.class);
    }

    @Test
    @DisplayName("임박 식재료 조회 시 RegisteredFood 리스트 반환")
    void findImminentFoods_SUCCESS() {
        // given
        String memberKey = FoodFixture.MEMBER_KEY;
        List<FoodEntity> foodEntities = List.of(FoodFixture.createFoodEntity(1L));

        FoodCategories mockFoodCategories = mock(FoodCategories.class);
        given(mockFoodCategories.getCategoryNames(anyLong())).willReturn(List.of("임박식품"));

        given(foodRepository.findImminentFoods(memberKey)).willReturn(foodEntities);
        given(foodCategoryReader.findNamesByFoodIds(anyList())).willReturn(mockFoodCategories);

        // when
        List<RegisteredFood> results = foodReader.findImminentFoods(memberKey);

        // then
        assertThat(results).hasSize(1);
        assertThat(results.getFirst().categoryNames()).contains("임박식품");
    }

    @Test
    @DisplayName("단순 식재료 조회 (find) 시 Food 도메인 객체 반환")
    void find_SUCCESS() {
        // given
        Long foodId = 1L;
        FoodEntity foodEntity = FoodFixture.createFoodEntity(foodId);

        given(foodRepository.findById(foodId)).willReturn(Optional.of(foodEntity));

        // when
        Food result = foodReader.find(foodId);

        // then
        assertThat(result).isInstanceOf(Food.class);
        assertThat(result.id()).isEqualTo(foodId);
    }
}