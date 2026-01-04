package com.foodkeeper.foodkeeperserver.food.implement;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.enums.EntityStatus;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodCategoryEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.FoodCategoryRepository;
import com.foodkeeper.foodkeeperserver.food.domain.FoodCategory;
import com.foodkeeper.foodkeeperserver.food.fixture.CategoryFixture;
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
public class CategoryManagerTest {

    @InjectMocks CategoryManager categoryManager;

    @Mock FoodCategoryRepository foodCategoryRepository;

    @Test
    @DisplayName("식재료 카테고리Id 리스트를 받아서 조회")
    void findFoodCategories_SUCCESS() throws Exception {
        //given
        List<Long> categoryIds = List.of(1L, 2L);
        List<FoodCategoryEntity> foodCategoryEntities = CategoryFixture.createCategoryEntity(categoryIds);

        given(foodCategoryRepository.findAllById(categoryIds)).willReturn(foodCategoryEntities);
        //when
        List<FoodCategory> foodCategoryList = categoryManager.findAllByIds(categoryIds);
        //then
        List<FoodCategory> expectedDomains = foodCategoryEntities.stream()
                .map(FoodCategoryEntity::toDomain)
                .toList();

        assertThat(foodCategoryList)
                .usingRecursiveComparison()
                .isEqualTo(expectedDomains);

    }

    @Test
    @DisplayName("카테고리 생성")
    void add_SUCCESS() throws Exception {
        //given
        FoodCategory foodCategory = CategoryFixture.createCategory(1L);
        FoodCategoryEntity entity = FoodCategoryEntity.from(foodCategory);
        given(foodCategoryRepository.save(any(FoodCategoryEntity.class))).willReturn(entity);
        //when
        categoryManager.addCategory(foodCategory.name(), foodCategory.memberKey());
        //then
        ArgumentCaptor<FoodCategoryEntity> captor = ArgumentCaptor.forClass(FoodCategoryEntity.class);
        verify(foodCategoryRepository).save(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo(foodCategory.name());
    }

    @Test
    @DisplayName("카테고리 리스트를 memberKey 로 조회")
    void findAllByMemberKey_SUCCESS() throws Exception {
        //given
        String memberKey = "memberKey";
        List<Long> categoryIds = List.of(1L, 2L);
        List<FoodCategoryEntity> foodCategoryEntities = CategoryFixture.createCategoryEntity(categoryIds);
        given(foodCategoryRepository.findAllByMemberKey(memberKey)).willReturn(foodCategoryEntities);
        //when
        List<FoodCategory> foodCategories = categoryManager.findAllByMemberKey(memberKey);
        //then
        assertThat(foodCategories.getFirst().id()).isEqualTo(1L);
        assertThat(foodCategories.getFirst().memberKey()).isEqualTo(memberKey);
    }

    @Test
    @DisplayName("카테고리 이름 수정")
    void updateCategory_SUCCESS() throws Exception {
        //given
        String updateName = "updated-name";
        String memberKey = "memberKey";
        FoodCategoryEntity entity = FoodCategoryEntity.from(CategoryFixture.createCategory(1L));
        given(foodCategoryRepository.findByIdAndMemberKey(1L, memberKey)).willReturn(Optional.ofNullable(entity));
        //when
        categoryManager.updateCategory(1L, updateName, memberKey);
        //then
        assertThat(entity.getName()).isEqualTo(updateName);
    }

    @Test
    @DisplayName("카테고리 삭제")
    void remove_SUCCESS() throws Exception {
        //given
        String memberKey = "memberKey";
        FoodCategoryEntity entity = FoodCategoryEntity.from(CategoryFixture.createCategory(1L));
        given(foodCategoryRepository.findByIdAndMemberKey(1L, memberKey)).willReturn(Optional.ofNullable(entity));
        //when
        categoryManager.removeCategory(1L,memberKey);
        //then
        assertThat(entity.getStatus()).isEqualTo(EntityStatus.DELETED);
    }
}
