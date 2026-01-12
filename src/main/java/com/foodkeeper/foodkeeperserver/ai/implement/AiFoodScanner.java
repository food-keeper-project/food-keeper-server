package com.foodkeeper.foodkeeperserver.ai.implement;

import com.foodkeeper.foodkeeperserver.ai.AiProcessor;
import com.foodkeeper.foodkeeperserver.food.domain.ScannedFood;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class AiFoodScanner {
    private final AiProcessor processor;

    @Value("classpath:prompts/ocr-scan-system-prompt.txt")
    private Resource systemPrompt;
    private String loadedSystemPrompt;

    @PostConstruct
    public void init() {
        try {
            this.loadedSystemPrompt = StreamUtils.copyToString(
                    systemPrompt.getInputStream(),
                    StandardCharsets.UTF_8
            );
        } catch (IOException e) {
            log.error("시스템 프롬프트 로딩 실패: {}", loadedSystemPrompt, e);
            throw new AppException(ErrorType.NAVER_CLOVA_PROMPT_ERROR);
        }
    }

    public ScannedFood parseOcrText(String ocrText) {
        return processor.executeClova(loadedSystemPrompt, ocrText, ScannedFood.class);
    }

}
