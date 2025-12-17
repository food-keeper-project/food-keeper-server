package com.foodkeeper.foodkeeperserver.recipe.business.request;

import com.foodkeeper.foodkeeperserver.recipe.domain.ClovaMessage;
import com.foodkeeper.foodkeeperserver.recipe.domain.AiType;

import java.util.List;

public record ClovaRequest(
        List<ClovaMessage> messages,
        Integer maxCompletionTokens
) {
    private static final int DEFAULT_TOKENS = 1000;

    public static ClovaRequest createPrompt(String systemPrompt, String userContent) {
        return new ClovaRequest(
                List.of(
                        ClovaMessage.toRequest(AiType.SYSTEM, systemPrompt),
                        ClovaMessage.toRequest(AiType.USER, userContent)
                ),
                DEFAULT_TOKENS);
    }
}
