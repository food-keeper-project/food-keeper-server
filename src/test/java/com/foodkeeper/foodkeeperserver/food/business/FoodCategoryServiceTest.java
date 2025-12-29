package com.foodkeeper.foodkeeperserver.food.business;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodCategoryEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.FoodCategoryRepository;
import com.foodkeeper.foodkeeperserver.food.domain.FoodCategory;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodCategoryRegister;
import com.foodkeeper.foodkeeperserver.food.fixture.CategoryFixture;
import com.foodkeeper.foodkeeperserver.food.implement.FoodCategoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FoodCategoryServiceTest {

    @InjectMocks
    FoodCategoryService foodCategoryService;
    @Mock
    FoodCategoryRepository foodCategoryRepository;

    @BeforeEach
    void setUp() {
        FoodCategoryManager foodCategoryManager = new FoodCategoryManager(foodCategoryRepository);
        foodCategoryService = new FoodCategoryService(foodCategoryManager);
    }

    @Test
    @DisplayName("카테고리 등록")
    void registerFoodCategory_SUCCESS() {
        // given
        FoodCategoryRegister request = new FoodCategoryRegister("유제품", "memberKey");
        FoodCategoryEntity entity = CategoryFixture.createCategoryEntity(1L);
        given(foodCategoryRepository.save(any(FoodCategoryEntity.class))).willReturn(entity);
        // when
        foodCategoryService.registerFoodCategory(request);
        // then
        ArgumentCaptor<FoodCategoryEntity> captor = ArgumentCaptor.forClass(FoodCategoryEntity.class);
        verify(foodCategoryRepository).save(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo(entity.getName());
    }

    @Test
    @DisplayName("회원 키로 모든 카테고리를 조회")
    void findAllByMemberKey_SUCCESS() {
        // given
        String memberKey = "memberKey";
        List<Long> categoryIds = List.of(1L, 2L);
        List<FoodCategoryEntity> foodCategoryEntities = CategoryFixture.createCategoryEntity(categoryIds);
        given(foodCategoryRepository.findAllByMemberKey(memberKey)).willReturn(foodCategoryEntities);

        // when
        List<FoodCategory> result = foodCategoryService.findAllByMemberKey(memberKey);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.getFirst().id()).isEqualTo(foodCategoryEntities.getFirst().getId());
    }

    @Test
    @DisplayName("카테고리 수정 시 매니저의 updateCategory가 호출된다")
    void updateCategory_SUCCESS() {
        // given
        Long id = 1L;
        String updateName = "updated-name";
        String memberKey = "memberKey";
        FoodCategoryEntity entity = CategoryFixture.createCategoryEntity(1L);
        given(foodCategoryRepository.findByIdAndMemberKey(id, memberKey)).willReturn(Optional.ofNullable(entity));
        // when
        foodCategoryService.updateCategory(id, updateName, memberKey);

        // then
        assertThat(entity.getName()).isEqualTo(updateName);
    }

}
