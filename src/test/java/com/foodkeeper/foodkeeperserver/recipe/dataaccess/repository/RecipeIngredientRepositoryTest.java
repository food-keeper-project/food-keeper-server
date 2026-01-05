package com.foodkeeper.foodkeeperserver.recipe.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.enums.EntityStatus;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.entity.RecipeIngredientEntity;
import com.foodkeeper.foodkeeperserver.support.repository.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class RecipeIngredientRepositoryTest extends RepositoryTest {

    @Autowired RecipeIngredientRepository recipeIngredientRepository;

    @Test
    @DisplayName("recipeId들에 해당하는 RecipeIngredient들을 삭제한다.")
    void deleteRecipeIngredientsByRecipeIds() {
        // given
        RecipeIngredientEntity ingredient1 = em.persist(new RecipeIngredientEntity("name1", "quantity", 1L));
        RecipeIngredientEntity ingredient2 = em.persist(new RecipeIngredientEntity("name1", "quantity", 2L));
        RecipeIngredientEntity ingredient3 = em.persist(new RecipeIngredientEntity("name1", "quantity", 3L));

        // when
        recipeIngredientRepository.deleteAllByRecipeIds(List.of(2L, 3L));
        em.flush();
        em.clear();

        // then
        Optional<RecipeIngredientEntity> foundIngredient1 = recipeIngredientRepository.findById(ingredient1.getId());
        Optional<RecipeIngredientEntity> foundIngredient2 = recipeIngredientRepository.findById(ingredient2.getId());
        Optional<RecipeIngredientEntity> foundIngredient3 = recipeIngredientRepository.findById(ingredient3.getId());
        assertThat(foundIngredient1).isNotEmpty();
        assertThat(foundIngredient1.get().getStatus()).isEqualTo(EntityStatus.ACTIVE);
        assertThat(foundIngredient1.get().getDeletedAt()).isNull();
        assertThat(foundIngredient2).isNotEmpty();
        assertThat(foundIngredient2.get().getStatus()).isEqualTo(EntityStatus.DELETED);
        assertThat(foundIngredient2.get().getDeletedAt()).isNotNull();
        assertThat(foundIngredient3).isNotEmpty();
        assertThat(foundIngredient3.get().getStatus()).isEqualTo(EntityStatus.DELETED);
        assertThat(foundIngredient3.get().getDeletedAt()).isNotNull();
    }

    @Test
    @DisplayName("recipeId들에 해당하는 RecipeIngredient들을 조회한다.")
    void findRecipeIngredientsByRecipeIds() {
        // given
        RecipeIngredientEntity ingredient1 = em.persist(new RecipeIngredientEntity("name1", "quantity", 1L));
        RecipeIngredientEntity ingredient2 = em.persist(new RecipeIngredientEntity("name1", "quantity", 1L));
        em.persist(new RecipeIngredientEntity("name1", "quantity", 2L));

        // when
        List<RecipeIngredientEntity> ingredients = recipeIngredientRepository.findByRecipeId(1L);

        // then
        assertThat(ingredients).hasSize(2);
        assertThat(ingredients).containsExactly(ingredient1, ingredient2);
    }
}