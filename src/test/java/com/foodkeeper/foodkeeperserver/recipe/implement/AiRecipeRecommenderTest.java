package com.foodkeeper.foodkeeperserver.recipe.implement;

import com.foodkeeper.foodkeeperserver.ai.AiProcessor;
import com.foodkeeper.foodkeeperserver.ai.implement.AiRecipeRecommender;
import com.foodkeeper.foodkeeperserver.recipe.domain.NewRecipe;
import com.foodkeeper.foodkeeperserver.recipe.domain.RecipeIngredient;
import com.foodkeeper.foodkeeperserver.recipe.domain.RecipeStep;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AiRecipeRecommenderTest {

    @Mock
    AiProcessor processor;

    AiRecipeRecommender aiRecipeRecommender;

    @BeforeEach
    void setUp() {
        aiRecipeRecommender = new AiRecipeRecommender(processor);
    }

    @Test
    @DisplayName("AI 레시피 추천 성공 시 NewRecipe를 반환한다")
    void getRecipeRecommendation_success() {
        NewRecipe expected = buildNewRecipe();
        given(processor.executeClova(any(), anyString(), eq(NewRecipe.class)))
                .willReturn(CompletableFuture.completedFuture(expected));

        NewRecipe result = aiRecipeRecommender
                .getRecipeRecommendation(List.of("계란", "당근"), List.of())
                .join();

        assertThat(result.menuName()).isEqualTo("요리 이름");
        assertThat(result.description()).isEqualTo("한 줄 소개");
        assertThat(result.cookMinutes()).isEqualTo(20);
        assertThat(result.recipeIngredients()).hasSize(1);
        assertThat(result.steps()).hasSize(1);
    }

    @Test
    @DisplayName("AI 호출 실패 시 AppException(NAVER_CLOVA_ERROR)로 변환된다")
    void getRecipeRecommendation_onFailure_throwsAppException() {
        given(processor.executeClova(any(), anyString(), eq(NewRecipe.class)))
                .willReturn(CompletableFuture.failedFuture(new ResourceAccessException("타임아웃")));

        CompletableFuture<NewRecipe> future = aiRecipeRecommender
                .getRecipeRecommendation(List.of("계란"), List.of());

        assertThatThrownBy(future::join)
                .isInstanceOf(CompletionException.class)
                .hasCauseInstanceOf(AppException.class);
    }

    @Test
    @DisplayName("excludedMenus가 있을 때 제약 조건이 프롬프트에 포함된다")
    void getRecipeRecommendation_withExcludedMenus_includesConstraintsInPrompt() {
        given(processor.executeClova(any(), anyString(), eq(NewRecipe.class)))
                .willReturn(CompletableFuture.completedFuture(buildNewRecipe()));

        ArgumentCaptor<String> userPromptCaptor = ArgumentCaptor.forClass(String.class);
        aiRecipeRecommender.getRecipeRecommendation(List.of("계란"), List.of("계란찜", "스크램블")).join();

        verify(processor).executeClova(any(), userPromptCaptor.capture(), eq(NewRecipe.class));
        assertThat(userPromptCaptor.getValue())
                .contains("계란찜")
                .contains("스크램블");
    }

    @Test
    @DisplayName("excludedMenus가 없을 때 제약 조건이 프롬프트에 포함되지 않는다")
    void getRecipeRecommendation_withNoExcludedMenus_noConstraintsInPrompt() {
        given(processor.executeClova(any(), anyString(), eq(NewRecipe.class)))
                .willReturn(CompletableFuture.completedFuture(buildNewRecipe()));

        ArgumentCaptor<String> userPromptCaptor = ArgumentCaptor.forClass(String.class);
        aiRecipeRecommender.getRecipeRecommendation(List.of("계란"), List.of()).join();

        verify(processor).executeClova(any(), userPromptCaptor.capture(), eq(NewRecipe.class));
        assertThat(userPromptCaptor.getValue()).doesNotContain("중요 제약 사항");
    }

    private NewRecipe buildNewRecipe() {
        return NewRecipe.builder()
                .menuName("요리 이름")
                .description("한 줄 소개")
                .cookMinutes(20)
                .recipeIngredients(List.of(new RecipeIngredient("재료명", "정량")))
                .steps(List.of(new RecipeStep("핵심요약", "조리법")))
                .build();
    }
}
