package com.foodkeeper.foodkeeperserver.infra.ai;

import com.foodkeeper.foodkeeperserver.infra.ai.domain.ClovaRequest;
import com.foodkeeper.foodkeeperserver.infra.ai.domain.ClovaResponse;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.ClovaClient;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
public abstract class AiProcessor<T> {

    private static final String BEARER = "Bearer ";

    @Value("${clova.api-key}")
    private String apiKey;

    protected String systemPrompt;
    private final ClovaClient clovaClient;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        Resource resource = getSystemPrompt();
        try {
            this.systemPrompt = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("시스템 프롬프트 로딩 실패: {}", resource.getDescription(), e);
            throw new AppException(ErrorType.NAVER_CLOVA_PROMPT_ERROR);
        }
    }

    protected T executeClova(String userPrompt) {
        ClovaRequest clovaRequest = ClovaRequest.createPrompt(systemPrompt, userPrompt);
        ClovaResponse clovaResponse = clovaClient.getAiResponse(BEARER + apiKey, clovaRequest);
        log.info("[CLOVA RESULT] {}", clovaResponse.result().message().content());

        String content = clovaResponse.getContent();
        return objectMapper.readValue(content, getResponseType());
    }

    protected abstract Class<T> getResponseType();

    protected abstract Resource getSystemPrompt();

}
