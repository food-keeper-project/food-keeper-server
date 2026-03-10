package com.foodkeeper.foodkeeperserver.ai;

import com.foodkeeper.foodkeeperserver.ai.implement.ClovaRetryExecutor;
import com.foodkeeper.foodkeeperserver.recipe.domain.NewRecipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.ResourceAccessException;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AiProcessorTest {

    @Mock
    ClovaRetryExecutor clovaRetryExecutor;

    AiProcessor aiProcessor;

    @BeforeEach
    void setUp() {
        aiProcessor = new AiProcessor(clovaRetryExecutor, new ObjectMapper());
    }

    @Test
    @DisplayName("마크다운으로 감싼 JSON 응답에서 내용을 추출해 역직렬화한다")
    void executeClova_parsesMarkdownWrappedJson() {
        String raw = """
                ## 레시피입니다
                ---
                ai recipe
                {
                  "menuName": "요리 이름",
                  "description": "한 줄 소개",
                  "cookMinutes": 20,
                  "recipeIngredients": [{ "name": "재료명", "quantity": "정량" }],
                  "steps": [{ "title": "핵심요약", "content": "조리법" }]
                }
                ---
                ### 결과: done
                """;
        given(clovaRetryExecutor.call(anyString(), anyString())).willReturn(raw);

        NewRecipe recipe = aiProcessor.executeClova("system", "user", NewRecipe.class).join();

        assertThat(recipe.menuName()).isEqualTo("요리 이름");
        assertThat(recipe.description()).isEqualTo("한 줄 소개");
        assertThat(recipe.cookMinutes()).isEqualTo(20);
        assertThat(recipe.recipeIngredients()).hasSize(1);
        assertThat(recipe.recipeIngredients().getFirst().name()).isEqualTo("재료명");
        assertThat(recipe.recipeIngredients().getFirst().quantity()).isEqualTo("정량");
        assertThat(recipe.steps().getFirst().title()).isEqualTo("핵심요약");
    }

    @Test
    @DisplayName("마크다운 코드 블록으로 감싼 JSON 응답을 추출해 역직렬화한다")
    void executeClova_parsesCodeBlockWrappedJson() {
        String raw = """
                ```json
                {
                  "menuName": "코드블록 요리",
                  "description": "설명",
                  "cookMinutes": 30,
                  "recipeIngredients": [],
                  "steps": []
                }
                ```
                """;
        given(clovaRetryExecutor.call(anyString(), anyString())).willReturn(raw);

        NewRecipe recipe = aiProcessor.executeClova("system", "user", NewRecipe.class).join();

        assertThat(recipe.menuName()).isEqualTo("코드블록 요리");
        assertThat(recipe.cookMinutes()).isEqualTo(30);
    }

    @Test
    @DisplayName("ClovaRetryExecutor 예외 발생 시 예외가 호출자에게 전파된다")
    void executeClova_propagatesExceptionFromRetryExecutor() {
        given(clovaRetryExecutor.call(anyString(), anyString()))
                .willThrow(new ResourceAccessException("API 타임아웃"));

        assertThatThrownBy(() -> aiProcessor.executeClova("system", "user", NewRecipe.class))
                .isInstanceOf(ResourceAccessException.class)
                .hasMessageContaining("API 타임아웃");
    }
}
