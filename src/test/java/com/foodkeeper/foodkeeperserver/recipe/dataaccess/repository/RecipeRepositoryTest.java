package com.foodkeeper.foodkeeperserver.recipe.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.enums.EntityStatus;
import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.foodkeeper.foodkeeperserver.common.domain.SliceObject;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.entity.RecipeEntity;
import com.foodkeeper.foodkeeperserver.support.repository.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class RecipeRepositoryTest extends RepositoryTest {

    @Autowired RecipeRepository recipeRepository;

    @Test
    @DisplayName("memberKey를 통해 Recipe를 페이지로 조회한다.")
    void findRecipesByMemberKeyWithPagination() {
        // given
        String memberKey = "memberKey";
        em.persist(new RecipeEntity("title1", "description", 1, memberKey));
        em.persist(new RecipeEntity("title2", "description", 1, memberKey));
        em.persist(new RecipeEntity("title3", "description", 1, memberKey));
        em.persist(new RecipeEntity("title4", "description", 1, memberKey));
        em.persist(new RecipeEntity("title5", "description", 1, memberKey));
        em.persist(new RecipeEntity("title6", "description", 1, memberKey));
        em.persist(new RecipeEntity("title7", "description", 1, memberKey));

        // when
        SliceObject<RecipeEntity> recipePage1 = recipeRepository.findRecipes(
                new Cursorable<>(null, 3), memberKey);
        SliceObject<RecipeEntity> recipePage2 = recipeRepository.findRecipes(
                new Cursorable<>(recipePage1.content().getLast().getId(), 3), memberKey);

        // then
        assertThat(recipePage1.content()).hasSize(3);
        assertThat(recipePage2.content()).hasSize(3);
    }

    @Test
    @DisplayName("memberKey에 해당하는 Member의 Recipe들을 삭제한다.")
    void deleteRecipesByMemberKey() {
        // given
        String memberKey = "memberKey";
        RecipeEntity recipe1 = em.persist(new RecipeEntity("title1", "description", 1, memberKey));
        RecipeEntity recipe2 = em.persist(new RecipeEntity("title2", "description", 1, memberKey));
        RecipeEntity recipe3 = em.persist(new RecipeEntity("title3", "description", 1, "memberKey2"));

        // when
        recipeRepository.deleteRecipes(memberKey);
        em.flush();
        em.clear();

        // then
        Optional<RecipeEntity> foundRecipe1 = recipeRepository.findById(recipe1.getId());
        Optional<RecipeEntity> foundRecipe2 = recipeRepository.findById(recipe2.getId());
        Optional<RecipeEntity> foundRecipe3 = recipeRepository.findById(recipe3.getId());

        assertThat(foundRecipe1).isNotEmpty();
        assertThat(foundRecipe1.get().getStatus()).isEqualTo(EntityStatus.DELETED);
        assertThat(foundRecipe1.get().getDeletedAt()).isNotNull();
        assertThat(foundRecipe2).isNotEmpty();
        assertThat(foundRecipe2.get().getStatus()).isEqualTo(EntityStatus.DELETED);
        assertThat(foundRecipe2.get().getDeletedAt()).isNotNull();
        assertThat(foundRecipe3).isNotEmpty();
        assertThat(foundRecipe3.get().getStatus()).isEqualTo(EntityStatus.ACTIVE);
        assertThat(foundRecipe3.get().getDeletedAt()).isNull();
    }
}