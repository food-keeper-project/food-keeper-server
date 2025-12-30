package com.foodkeeper.foodkeeperserver.recipe.implement;

import com.foodkeeper.foodkeeperserver.recipe.dataaccess.ClovaClient;
import com.foodkeeper.foodkeeperserver.recipe.domain.AiType;
import com.foodkeeper.foodkeeperserver.recipe.domain.Recipe;
import com.foodkeeper.foodkeeperserver.recipe.domain.clova.ClovaMessage;
import com.foodkeeper.foodkeeperserver.recipe.domain.clova.ClovaResponse;
import com.foodkeeper.foodkeeperserver.recipe.domain.clova.ClovaResponseStatus;
import com.foodkeeper.foodkeeperserver.recipe.domain.clova.ClovaResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AiRecipeRecommenderTest {

    @Mock ClovaClient clovaClient;
    AiRecipeRecommender aiRecipeRecommender;

    @BeforeEach
    void setUp() {
        aiRecipeRecommender = new AiRecipeRecommender(clovaClient, new ObjectMapper());
    }

    @Test
    @DisplayName("ai를 통해 레시피를 json 형태로 추천받는다")
    void aiRecommendRecipeWithJson() {
        String clovaContent = """
                ## 레시피입니다
                ---
                ai recipe
                {
                  "menuName": "요리 이름",
                  "description": "요리에 대한 매력적인 한 줄 소개",
                  "totalTime": "20분",
                  "ingredients": [
                    { "name": "재료명", "quantity": "정량" }
                  ],
                  "steps": [
                    {
                      "title": "단계별 핵심 요약",
                      "content": "상세 조리법"
                    },
                    {
                      "title": "제목",
                      "content": "내용"
                    }
                  ]
                }
                ---
                ### 레시피 결과: abcdefg
                """;

        ClovaMessage clovaMessage = new ClovaMessage(AiType.SYSTEM, clovaContent);
        ClovaResponse clovaResponse = new ClovaResponse(
                new ClovaResponseStatus("code", "message"),
                new ClovaResult(clovaMessage));
        given(clovaClient.getRecipe(anyString(), any())).willReturn(clovaResponse);

        Recipe recipe = aiRecipeRecommender.getRecipeRecommendation(List.of("test"), List.of("test"));

        assertThat(recipe.menuName()).isEqualTo("요리 이름");
        assertThat(recipe.description()).isEqualTo("요리에 대한 매력적인 한 줄 소개");
        assertThat(recipe.totalTime()).isEqualTo("20분");
        assertThat(recipe.ingredients()).hasSize(1);
        assertThat(recipe.ingredients().getFirst().name()).isEqualTo("재료명");
        assertThat(recipe.ingredients().getFirst().quantity()).isEqualTo("정량");
        assertThat(recipe.steps()).hasSize(2);
        assertThat(recipe.steps().getFirst().title()).isEqualTo("단계별 핵심 요약");
        assertThat(recipe.steps().getFirst().content()).isEqualTo("상세 조리법");
        assertThat(recipe.steps().get(1).title()).isEqualTo("제목");
        assertThat(recipe.steps().get(1).content()).isEqualTo("내용");
    }
}