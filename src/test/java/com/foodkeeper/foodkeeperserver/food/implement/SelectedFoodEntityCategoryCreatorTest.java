package com.foodkeeper.foodkeeperserver.food.implement;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.SelectedFoodCategoryEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.SelectedFoodCategoryRepository;
import com.foodkeeper.foodkeeperserver.food.domain.SelectedFoodCategory;
import com.foodkeeper.foodkeeperserver.food.fixture.SelectedFoodCategoryFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SelectedFoodEntityCategoryCreatorTest {

    @InjectMocks
    private SelectedFoodCategoryManager selectedFoodCategoryManager;

    @Mock
    private SelectedFoodCategoryRepository selectedFoodCategoryRepository;

    @Test
    @DisplayName("카테고리 생성 시 리포지토리 호출 및 결과 반환")
    void save_SUCCESS() throws Exception {
        //given
        SelectedFoodCategory selectedCategory = SelectedFoodCategoryFixture.createSelectedCategory(1L, 1L);
        SelectedFoodCategoryEntity selectedFoodCategoryEntity = SelectedFoodCategoryFixture.createSelectedCategoryEntity(1L, 1L);

        given(selectedFoodCategoryRepository.save(any(SelectedFoodCategoryEntity.class))).willReturn(selectedFoodCategoryEntity);

        //when
        selectedFoodCategoryManager.save(selectedCategory);

        //then
        ArgumentCaptor<SelectedFoodCategoryEntity> captor = ArgumentCaptor.forClass(SelectedFoodCategoryEntity.class);
        verify(selectedFoodCategoryRepository).save(captor.capture());

        SelectedFoodCategoryEntity capturedEntity = captor.getValue();

        assertThat(capturedEntity.getFoodId()).isEqualTo(1L);
        assertThat(capturedEntity.getFoodCategoryId()).isEqualTo(1L);
    }
}
