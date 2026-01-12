package com.foodkeeper.foodkeeperserver.ai;

import com.foodkeeper.foodkeeperserver.ai.domain.ClovaRequest;
import com.foodkeeper.foodkeeperserver.ai.domain.ClovaResponse;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.ClovaClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class AiProcessor {

    private static final String BEARER = "Bearer ";

    @Value("${clova.api-key}")
    private String apiKey;

    private final ClovaClient clovaClient;
    private final ObjectMapper objectMapper;

    public <T> T executeClova(String systemPrompt, String userPrompt, Class<T> responseType) {
        ClovaRequest clovaRequest = ClovaRequest.createPrompt(systemPrompt, userPrompt);
        ClovaResponse clovaResponse = clovaClient.getAiResponse(BEARER + apiKey, clovaRequest);
        log.info("[CLOVA RESULT] {}", clovaResponse.result().message().content());

        String content = clovaResponse.getContent();
        return objectMapper.readValue(content, responseType);
    }

}
