package com.foodkeeper.foodkeeperserver.food.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.SelectedFoodCategoryEntity;
import com.foodkeeper.foodkeeperserver.support.repository.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class SelectedFoodCategoryRepositoryTest extends RepositoryTest {

    @Autowired SelectedFoodCategoryRepository selectedFoodCategoryRepository;

    @Test
    @DisplayName("foodId에 해당하는 SelectedFoodCategory들을 삭제한다.")
    void deleteSelectedFoodCategoriesByFoodId() {
        // given
        long foodCategoryId = 1L;
        SelectedFoodCategoryEntity category1 = em.persist(new SelectedFoodCategoryEntity(1L, foodCategoryId));
        SelectedFoodCategoryEntity category2 = em.persist(new SelectedFoodCategoryEntity(2L, foodCategoryId));
        SelectedFoodCategoryEntity category3 = em.persist(new SelectedFoodCategoryEntity(3L, foodCategoryId));

        // when
        selectedFoodCategoryRepository.deleteAllByFoodIds(List.of(1L, 3L));

        // then
        Optional<SelectedFoodCategoryEntity> foundCategory1 = selectedFoodCategoryRepository.findById(category1.getId());
        Optional<SelectedFoodCategoryEntity> foundCategory2 = selectedFoodCategoryRepository.findById(category2.getId());
        Optional<SelectedFoodCategoryEntity> foundCategory3 = selectedFoodCategoryRepository.findById(category3.getId());
        assertThat(foundCategory1).isEmpty();
        assertThat(foundCategory2).isNotEmpty();
        assertThat(foundCategory3).isEmpty();
    }
}