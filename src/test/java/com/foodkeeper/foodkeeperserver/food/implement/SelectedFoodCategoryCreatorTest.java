package com.foodkeeper.foodkeeperserver.food.implement;

import com.foodkeeper.foodkeeperserver.food.entity.SelectedFoodCategory;
import com.foodkeeper.foodkeeperserver.food.fixture.SelectedFoodCategoryFixture;
import com.foodkeeper.foodkeeperserver.food.repository.SelectedFoodCategoryRepository;
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
public class SelectedFoodCategoryCreatorTest {

    @InjectMocks
    private SelectedFoodCategoryCreator selectedFoodCategoryCreator;

    @Mock
    private SelectedFoodCategoryRepository selectedFoodCategoryRepository;

    @Test
    @DisplayName("카테고리 생성 시 리포지토리 호출 및 결과 반환")
    void save_SUCCESS() throws Exception {
        //given
        SelectedFoodCategory selectedCategory = SelectedFoodCategoryFixture.createSelectedFoodCategory(1L,1L,1L);
        SelectedFoodCategory savedSelectedCategory = SelectedFoodCategoryFixture.createSelectedFoodCategory(1L,1L,1L);

        given(selectedFoodCategoryRepository.save(any(SelectedFoodCategory.class))).willReturn(savedSelectedCategory);
        //when
        Long resultId = selectedFoodCategoryCreator.save(1L,1L);
        //then
        assertThat(resultId).isNotNull();
    }
}
