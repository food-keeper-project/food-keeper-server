package com.foodkeeper.foodkeeperserver.recipe.implement;

import com.foodkeeper.foodkeeperserver.recipe.business.request.ClovaRequest;
import com.foodkeeper.foodkeeperserver.recipe.business.response.ClovaResponse;
import com.foodkeeper.foodkeeperserver.recipe.controller.v1.ClovaClient;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClovaService {

    @Value("${clova.api-key}")
    private String apiKey;

    @Value("classpath:prompts/recipe-system-prompt.txt")
    private Resource systemPromptResource;

    private String systemPrompt;
    private final ClovaClient clovaClient;

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

    public String getRecipeRecommendation(List<String> ingredients, List<String> excludedMenus) throws IOException {

        String userPrompt = removeDuplicateFood(ingredients, excludedMenus);

        ClovaRequest request = ClovaRequest.createPrompt(systemPrompt, userPrompt);

        ClovaResponse clovaResponse = clovaClient.getRecipe("Bearer " + apiKey, request);
        return clovaResponse.getContent();
    }

    private String removeDuplicateFood(List<String> ingredients, List<String> excludedMenus) {
        StringBuilder promptBuilder = new StringBuilder();

        String ingredientsStr = String.join(", ", ingredients);
        promptBuilder.append(String.format("현재 가지고 있는 재료 리스트: [%s].\n", ingredientsStr));

        if (excludedMenus != null && !excludedMenus.isEmpty()) {
            String bannedMenus = String.join(", ", excludedMenus);
            promptBuilder.append("\n[중요 제약 사항]\n");
            promptBuilder.append(String.format("사용자는 이미 다음 요리들을 추천받았습니다: **[%s]**\n", bannedMenus));
            promptBuilder.append("1. 위 목록에 있는 요리와 이름이 같거나 매우 유사한 요리는 **절대 추천하지 마세요.**\n");
            promptBuilder.append("2. 위 요리들과는 조리법(볶음/탕/찜 등)이나 식감이 다른 새로운 메뉴를 선정하세요.\n");
        }

        promptBuilder.append("\n위 조건을 만족하는 레시피 1개를 정해진 JSON 형식으로 답변해줘.");

        return promptBuilder.toString();
    }
}
