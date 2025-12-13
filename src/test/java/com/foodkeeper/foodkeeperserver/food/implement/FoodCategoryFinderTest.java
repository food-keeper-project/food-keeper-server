package com.foodkeeper.foodkeeperserver.food.implement;

import com.foodkeeper.foodkeeperserver.food.entity.FoodCategory;
import com.foodkeeper.foodkeeperserver.food.fixture.CategoryFixture;
import com.foodkeeper.foodkeeperserver.food.repository.FoodCategoryRepository;
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
public class FoodCategoryFinderTest {

    @InjectMocks
    private FoodCategoryFinder foodCategoryFinder;

    @Mock
    private FoodCategoryRepository foodCategoryRepository;

    @Test
    @DisplayName("식재료 카테고리Id 리스트를 받아서 조회")
    void findFoodCategories_SUCCESS() throws Exception {
        //given
        List<Long> categoryIds = List.of(1L,2L);
        List<FoodCategory> foodCategories = CategoryFixture.createCategory(categoryIds);

        given(foodCategoryRepository.findAllById(categoryIds)).willReturn(foodCategories);
        //when
        List<FoodCategory> foodCategoryList = foodCategoryFinder.findAll(categoryIds);
        //then
        assertThat(foodCategoryList).isEqualTo(foodCategories);

    }
}
