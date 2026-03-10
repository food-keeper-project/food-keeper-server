package com.foodkeeper.foodkeeperserver.ai.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClovaRetryExecutor {

    private final ChatClient chatClient;

    @Retryable(
            retryFor = {ResourceAccessException.class, HttpServerErrorException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2.0)
    )
    public String call(String systemPrompt, String userPrompt) {
        log.info("[Clova 호출 시도]");
        return chatClient.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .call()
                .content();
    }
}
