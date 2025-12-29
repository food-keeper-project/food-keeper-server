package com.foodkeeper.foodkeeperserver.food.implement;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.enums.EntityStatus;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.SelectedFoodCategoryEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.FoodRepository;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.SelectedFoodCategoryRepository;
import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.fixture.FoodFixture;
import com.foodkeeper.foodkeeperserver.food.fixture.SelectedFoodCategoryFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FoodManagerTest {

    @InjectMocks FoodManager foodManager;
    @Mock FoodRepository foodRepository;
    @Mock SelectedFoodCategoryRepository selectedFoodCategoryRepository;

    @BeforeEach
    void setUp() {
        SelectedFoodCategoryManager selectedFoodCategoryManager = new SelectedFoodCategoryManager(selectedFoodCategoryRepository);

        foodManager = new FoodManager(foodRepository,selectedFoodCategoryManager);
    }

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
    @DisplayName("식재료 삭제")
    void removeFood_SUCCESS() throws Exception {
        //given

        FoodEntity foodEntity = FoodFixture.createFoodEntity(1L);
        SelectedFoodCategoryEntity category = SelectedFoodCategoryFixture.createSelectedCategoryEntity(1L,1L);
        given(foodRepository.findByIdAndMemberKey(1L, FoodFixture.MEMBER_KEY)).willReturn(Optional.of(foodEntity));
        willDoNothing().given(selectedFoodCategoryRepository).deleteAllByFoodId(1L);
        //when
        Food result = foodManager.removeFood(1L, FoodFixture.MEMBER_KEY);
        //then
        verify(selectedFoodCategoryRepository).deleteAllByFoodId(1L);
        assertThat(result.status()).isEqualTo(EntityStatus.DELETED);
    }


}
