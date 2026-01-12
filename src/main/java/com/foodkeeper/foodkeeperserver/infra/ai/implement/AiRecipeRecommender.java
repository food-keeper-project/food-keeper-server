package com.foodkeeper.foodkeeperserver.infra.ai.implement;

import com.foodkeeper.foodkeeperserver.infra.ai.AiProcessor;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.ClovaClient;
import com.foodkeeper.foodkeeperserver.recipe.domain.NewRecipe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
public class AiRecipeRecommender extends AiProcessor<NewRecipe> {

    @Value("classpath:prompts/recipe-system-prompt.txt")
    private Resource systemPrompt;

    public AiRecipeRecommender(ClovaClient clovaClient, ObjectMapper objectMapper) {
        super(clovaClient, objectMapper);
    }

    public NewRecipe getRecipeRecommendation(List<String> ingredients, List<String> excludedMenus) {
        String userPrompt = removeDuplicateFood(ingredients, excludedMenus);

        return executeClova(userPrompt);
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

    @Override
    protected Class<NewRecipe> getResponseType() {
        return NewRecipe.class;
    }

    @Override
    protected Resource getSystemPrompt() {
        return this.systemPrompt;
    }
}
