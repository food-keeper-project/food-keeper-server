package com.foodkeeper.foodkeeperserver.recipe.business.request;

import com.foodkeeper.foodkeeperserver.recipe.domain.AiType;
import com.foodkeeper.foodkeeperserver.recipe.domain.ClovaMessage;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ClovaRequest(
        @NotNull List<ClovaMessage> messages,
        @NotNull Integer maxTokens
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
