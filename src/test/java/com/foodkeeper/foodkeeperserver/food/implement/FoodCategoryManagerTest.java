package com.foodkeeper.foodkeeperserver.food.implement;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodCategoryEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.FoodCategoryRepository;
import com.foodkeeper.foodkeeperserver.food.domain.FoodCategory;
import com.foodkeeper.foodkeeperserver.food.fixture.CategoryFixture;
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
public class FoodCategoryManagerTest {

    @InjectMocks
    private FoodCategoryManager foodCategoryManager;

    @Mock
    private FoodCategoryRepository foodCategoryRepository;

    @Test
    @DisplayName("식재료 카테고리Id 리스트를 받아서 조회")
    void findFoodCategories_SUCCESS() {
        //given
        List<Long> categoryIds = List.of(1L, 2L);
        List<FoodCategoryEntity> foodCategoryEntities = CategoryFixture.createCategoryEntity(categoryIds);

        given(foodCategoryRepository.findAllById(categoryIds)).willReturn(foodCategoryEntities);

        //when
        List<FoodCategory> foodCategoryList = foodCategoryManager.findAll(categoryIds);

        //then
        List<FoodCategory> expectedDomains = foodCategoryEntities.stream()
                .map(FoodCategoryEntity::toDomain)
                .toList();

        assertThat(foodCategoryList)
                .usingRecursiveComparison()
                .isEqualTo(expectedDomains);

    }
}
