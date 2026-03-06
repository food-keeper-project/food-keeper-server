package com.foodkeeper.foodkeeperserver.ai;

import com.foodkeeper.foodkeeperserver.ai.implement.ClovaRetryExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class AiProcessor {

    private static final String EMPTY_JSON = "{}";
    private static final Pattern JSON_PATTERN = Pattern.compile("(\\{.*\\}|\\[.*\\])", Pattern.DOTALL);
    private static final Pattern MARKDOWN_CODE_BLOCK = Pattern.compile("(?s)```(?:json)?\\s*(.*?)\\s*```");
    private static final Pattern UNQUOTED_KEY = Pattern.compile("(?<![\"'])(\\w+)\\s*:");

    private final ClovaRetryExecutor clovaRetryExecutor;
    private final ObjectMapper objectMapper;

    @Async("clovaExecutor")
    public <T> CompletableFuture<T> executeClova(String systemPrompt, String userPrompt, Class<T> responseType) {
        String raw = clovaRetryExecutor.call(systemPrompt, userPrompt);
        log.info("[CLOVA RESULT] {}", raw);
        String cleaned = cleanJsonContent(raw);
        T result = objectMapper.readValue(cleaned, responseType);
        return CompletableFuture.completedFuture(result);
    }

    private String cleanJsonContent(String rawContent) {
        if (rawContent == null) return EMPTY_JSON;

        String content = MARKDOWN_CODE_BLOCK.matcher(rawContent).replaceAll("$1");
        content = UNQUOTED_KEY.matcher(content).replaceAll("\"$1\":");

        Matcher matcher = JSON_PATTERN.matcher(content);
        if (matcher.find()) {
            return matcher.group().trim();
        }

        return EMPTY_JSON;
    }
}
