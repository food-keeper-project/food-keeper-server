package com.foodkeeper.foodkeeperserver.food.implement;

import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.foodkeeper.foodkeeperserver.common.domain.SliceObject;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodCategoryEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.SelectedFoodCategoryEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.FoodCategoryRepository;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.SelectedFoodCategoryRepository;
import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.domain.RegisteredFood;
import com.foodkeeper.foodkeeperserver.food.fixture.CategoryFixture;
import com.foodkeeper.foodkeeperserver.food.fixture.FoodFixture;
import com.foodkeeper.foodkeeperserver.food.fixture.SelectedFoodCategoryFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class FoodProviderTest {

    @InjectMocks
    FoodProvider foodProvider;
    @Mock
    FoodCategoryRepository foodCategoryRepository;
    @Mock
    SelectedFoodCategoryRepository selectedFoodCategoryRepository;

    @BeforeEach
    void setUp() {
        FoodCategoryManager foodCategoryManager = new FoodCategoryManager(foodCategoryRepository);
        SelectedFoodCategoryManager selectedFoodCategoryManager = new SelectedFoodCategoryManager(selectedFoodCategoryRepository);

        foodProvider = new FoodProvider(foodCategoryManager, selectedFoodCategoryManager);
    }

    @Test
    @DisplayName("음식 리스트 받고 카테고리 이름 매핑")
    void getFoodList_SUCCESS() throws Exception {
        //given
        Cursorable<Long> cursorable = new Cursorable<>(1L, 2);
        List<Long> foodIds = List.of(1L, 2L);
        List<Food> foods = List.of(FoodFixture.createFood(1L), FoodFixture.createFood(2L));
        SliceObject<Food> foodSlice = new SliceObject<>(foods, cursorable, false);

        List<SelectedFoodCategoryEntity> selectedEntities = List.of(
                SelectedFoodCategoryFixture.createSelectedCategoryEntity(1L, 2L)
        );
        given(selectedFoodCategoryRepository.findByFoodIdIn(foodIds))
                .willReturn(selectedEntities);

        List<FoodCategoryEntity> categoryEntities = List.of(
                CategoryFixture.createCategoryEntity(2L)
        );
        given(foodCategoryRepository.findAllByIdIn(List.of(2L)))
                .willReturn(categoryEntities);

        // when
        SliceObject<RegisteredFood> result = foodProvider.getFoodList(foodSlice);

        // then
        assertThat(result.content()).hasSize(2);
        assertThat(result.content().getFirst().categoryNames()).contains("유제품");
    }

    @Test
    @DisplayName("음식 단일 조회 후 카테고리 이름 매핑")
    void getFood_SUCCESS() throws Exception {
        //given
        Food food = FoodFixture.createFood(1L);

        List<SelectedFoodCategoryEntity> selectedEntities = List.of(
                SelectedFoodCategoryFixture.createSelectedCategoryEntity(1L, 2L)
        );
        given(selectedFoodCategoryRepository.findByFoodId(food.id()))
                .willReturn(selectedEntities);

        List<FoodCategoryEntity> categoryEntities = List.of(
                CategoryFixture.createCategoryEntity(2L)
        );
        given(foodCategoryRepository.findAllByIdIn(List.of(2L)))
                .willReturn(categoryEntities);

        //when
        RegisteredFood result = foodProvider.getFood(food);

        //then
        assertThat(result.categoryNames()).contains("유제품");
        assertThat(result.name()).isEqualTo(food.name());
    }

    @Test
    @DisplayName("음식 리스트 조회 후 카테고리 이름 매핑")
    void getAllFoods_SUCCESS() throws Exception {
        //given
        List<Long> foodIds = List.of(1L, 2L);
        List<Food> foods = List.of(FoodFixture.createFood(1L), FoodFixture.createFood(2L));

        List<SelectedFoodCategoryEntity> selectedEntities = List.of(
                SelectedFoodCategoryFixture.createSelectedCategoryEntity(1L, 2L)
        );
        given(selectedFoodCategoryRepository.findByFoodIdIn(foodIds))
                .willReturn(selectedEntities);

        List<FoodCategoryEntity> categoryEntities = List.of(
                CategoryFixture.createCategoryEntity(2L)
        );
        given(foodCategoryRepository.findAllByIdIn(List.of(2L)))
                .willReturn(categoryEntities);

        //when
        List<RegisteredFood> result = foodProvider.getAllFoods(foods);

        //then
        assertThat(result.getFirst().categoryNames()).contains("유제품");
        assertThat(result.getFirst().name()).isEqualTo(foods.getFirst().name());
    }
}

