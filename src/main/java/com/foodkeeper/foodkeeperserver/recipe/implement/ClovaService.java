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

    public String getRecipeRecommendation(List<String> ingredients) throws IOException {
        String ingredientsStr = String.join(", ", ingredients);
        String userPrompt = "현재 가지고 있는 재료 리스트 : [" + ingredientsStr + "]. 이 재료들로 레시피 추천해줘";

        ClovaRequest request = ClovaRequest.createPrompt(systemPrompt, userPrompt);

        ClovaResponse clovaResponse = clovaClient.getRecipe( "Bearer " + apiKey, request);
        return clovaResponse.getContent();
    }

}
