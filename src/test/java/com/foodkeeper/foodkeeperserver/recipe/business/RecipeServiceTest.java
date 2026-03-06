package com.foodkeeper.foodkeeperserver.recipe.business;

import com.foodkeeper.foodkeeperserver.ai.implement.AiRecipeRecommender;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.entity.RecipeEntity;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.repository.RecipeIngredientRepository;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.repository.RecipeRepository;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.repository.RecipeStepRepository;
import com.foodkeeper.foodkeeperserver.recipe.domain.NewRecipe;
import com.foodkeeper.foodkeeperserver.recipe.domain.RecipeIngredient;
import com.foodkeeper.foodkeeperserver.recipe.domain.RecipeStep;
import com.foodkeeper.foodkeeperserver.recipe.fixture.RecipeEntityFixture;
import com.foodkeeper.foodkeeperserver.recipe.implement.RecipeFinder;
import com.foodkeeper.foodkeeperserver.recipe.implement.RecipeManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock
    AiRecipeRecommender aiRecipeRecommender;
    @Mock
    RecipeRepository recipeRepository;
    @Mock
    RecipeStepRepository recipeStepRepository;
    @Mock
    RecipeIngredientRepository recipeIngredientRepository;

    RecipeService recipeService;

    @BeforeEach
    void setUp() {
        RecipeManager recipeManager = new RecipeManager(recipeRepository, recipeIngredientRepository, recipeStepRepository);
        RecipeFinder recipeFinder = new RecipeFinder(recipeRepository, recipeStepRepository, recipeIngredientRepository);
        recipeService = new RecipeService(aiRecipeRecommender, recipeManager, recipeFinder);
    }

    @Test
    @DisplayName("AI를 통해 레시피를 추천받는다")
    void recommendRecipe_returnsNewRecipe() {
        NewRecipe expected = NewRecipe.builder()
                .menuName("요리 이름")
                .description("한 줄 소개")
                .cookMinutes(20)
                .steps(List.of(new RecipeStep("단계별 핵심 요약", "상세 조리법")))
                .recipeIngredients(List.of(new RecipeIngredient("재료명", "정량")))
                .build();
        given(aiRecipeRecommender.getRecipeRecommendation(anyList(), anyList()))
                .willReturn(CompletableFuture.completedFuture(expected));

        NewRecipe result = recipeService.recommendRecipe(List.of("계란", "당근"), List.of()).join();

        assertThat(result.menuName()).isEqualTo("요리 이름");
        assertThat(result.description()).isEqualTo("한 줄 소개");
        assertThat(result.cookMinutes()).isEqualTo(20);
        assertThat(result.recipeIngredients()).hasSize(1);
        assertThat(result.recipeIngredients().getFirst().name()).isEqualTo("재료명");
        assertThat(result.steps()).hasSize(1);
        assertThat(result.steps().getFirst().title()).isEqualTo("단계별 핵심 요약");
    }

    @Test
    @DisplayName("레시피를 저장한다")
    void registerRecipe() {
        long recipeId = 1L;
        String memberKey = "memberKey";
        RecipeEntity recipeEntity = spy(RecipeEntityFixture.DEFAULT.get(memberKey));
        given(recipeEntity.getId()).willReturn(recipeId);
        given(recipeRepository.save(any(RecipeEntity.class))).willReturn(recipeEntity);

        NewRecipe newRecipe = NewRecipe.builder()
                .menuName("menu")
                .description("desc")
                .cookMinutes(20)
                .steps(List.of(new RecipeStep("title", "content")))
                .recipeIngredients(List.of(new RecipeIngredient("name", "quantity")))
                .build();

        Long savedRecipeId = recipeService.registerRecipe(newRecipe, memberKey);

        assertThat(savedRecipeId).isEqualTo(recipeId);
    }
}
