package com.foodkeeper.foodkeeperserver.recipe.implement;

import com.foodkeeper.foodkeeperserver.recipe.dataaccess.ClovaClient;
import com.foodkeeper.foodkeeperserver.recipe.domain.Recipe;
import com.foodkeeper.foodkeeperserver.recipe.domain.clova.ClovaRequest;
import com.foodkeeper.foodkeeperserver.recipe.domain.clova.ClovaResponse;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiRecipeRecommender {

    private static final String BEARER = "Bearer ";

    @Value("${clova.api-key}")
    private String apiKey;

    @Value("classpath:prompts/recipe-system-prompt.txt")
    private Resource systemPromptResource;

    private String systemPrompt;

    private final ClovaClient clovaClient;
    private final ObjectMapper objectMapper;

    // 텍스트 파일 빈 생성 시 메모리에 저장
    @PostConstruct
    public void init() {
        try {
            this.systemPrompt = StreamUtils.copyToString(systemPromptResource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("시스템 프롬프트 로딩 실패", e);
            throw new AppException(ErrorType.NAVER_CLOVA_PROMPT_ERROR);
        }
    }

    public Recipe getRecipeRecommendation(List<String> ingredients, List<String> excludedMenus) {
        String userPrompt = removeDuplicateFood(ingredients, excludedMenus);

        ClovaRequest clovaRequest = ClovaRequest.createPrompt(systemPrompt, userPrompt);
        ClovaResponse clovaResponse = clovaClient.getRecipe(BEARER + apiKey, clovaRequest);

        String content = clovaResponse.getContent();
        return objectMapper.readValue(content, Recipe.class);
    }

    private String removeDuplicateFood(List<String> ingredients, List<String> excludedMenus) {
        String ingredientsStr = String.join(", ", ingredients);

        String prompt = """
                현재 가지고 있는 재료 리스트: [%s].
                """.formatted(ingredientsStr);

        if (excludedMenus != null && !excludedMenus.isEmpty()) {
            String bannedMenus = String.join(", ", excludedMenus);
            String constraints = """
                    [중요 제약 사항]
                    사용자는 이미 다음 요리들을 추천받았습니다: **[%s]**
                    1. 위 목록에 있는 요리와 이름이 같거나 매우 유사한 요리는 **절대 추천하지 마세요.**
                    2. 위 요리들과는 조리법(볶음/탕/찜 등)이나 식감이 다른 새로운 메뉴를 선정하세요.
                    """.formatted(bannedMenus);
            prompt += constraints;
        }

        return prompt + "\n위 조건을 만족하는 레시피 1개를 정해진 JSON 형식으로 답변해줘.";
    }
}
