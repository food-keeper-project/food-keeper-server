package com.foodkeeper.foodkeeperserver.recipe.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.enums.EntityStatus;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.entity.RecipeStepEntity;
import com.foodkeeper.foodkeeperserver.support.repository.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class RecipeStepRepositoryTest extends RepositoryTest {

    @Autowired RecipeStepRepository recipeStepRepository;

    @Test
    @DisplayName("recipeId들에 해당하는 RecipeStep들을 삭제한다.")
    void deleteRecipeStepsByRecipeIds() {
        // given
        RecipeStepEntity step1 = em.persist(new RecipeStepEntity("title1", "content", 1L));
        RecipeStepEntity step2 = em.persist(new RecipeStepEntity("title1", "content", 2L));
        RecipeStepEntity step3 = em.persist(new RecipeStepEntity("title1", "content", 3L));

        // when
        recipeStepRepository.deleteAllByRecipeIds(List.of(2L, 3L));
        em.flush();
        em.clear();

        // then
        Optional<RecipeStepEntity> foundStep1 = recipeStepRepository.findById(step1.getId());
        Optional<RecipeStepEntity> foundStep2 = recipeStepRepository.findById(step2.getId());
        Optional<RecipeStepEntity> foundStep3 = recipeStepRepository.findById(step3.getId());
        assertThat(foundStep1).isNotEmpty();
        assertThat(foundStep1.get().getStatus()).isEqualTo(EntityStatus.ACTIVE);
        assertThat(foundStep1.get().getDeletedAt()).isNull();
        assertThat(foundStep2).isNotEmpty();
        assertThat(foundStep2.get().getStatus()).isEqualTo(EntityStatus.DELETED);
        assertThat(foundStep2.get().getDeletedAt()).isNotNull();
        assertThat(foundStep3).isNotEmpty();
        assertThat(foundStep3.get().getStatus()).isEqualTo(EntityStatus.DELETED);
        assertThat(foundStep3.get().getDeletedAt()).isNotNull();
    }

    @Test
    @DisplayName("recipeId들에 해당하는 RecipeStep들을 조회한다.")
    void findRecipeIngredientsByRecipeIds() {
        // given
        RecipeStepEntity step1 = em.persist(new RecipeStepEntity("name1", "quantity", 1L));
        RecipeStepEntity step2 = em.persist(new RecipeStepEntity("name1", "quantity", 1L));
        em.persist(new RecipeStepEntity("name1", "quantity", 2L));

        // when
        List<RecipeStepEntity> steps = recipeStepRepository.findByRecipeId(1L);

        // then
        assertThat(steps).hasSize(2);
        assertThat(steps).containsExactly(step1, step2);
    }
}