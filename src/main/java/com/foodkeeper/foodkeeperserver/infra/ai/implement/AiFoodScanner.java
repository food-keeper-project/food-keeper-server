package com.foodkeeper.foodkeeperserver.infra.ai.implement;

import com.foodkeeper.foodkeeperserver.food.domain.FoodScan;
import com.foodkeeper.foodkeeperserver.infra.ai.AiProcessor;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.ClovaClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class AiFoodScanner extends AiProcessor<FoodScan> {

    @Value("classpath:prompts/ocr-scan-system-prompt.txt")
    private Resource systemPrompt;

    public AiFoodScanner(ClovaClient clovaClient, ObjectMapper objectMapper) {
        super(clovaClient, objectMapper);
    }

    public FoodScan parseOcrText(String ocrText) {
        return executeClova(ocrText);
    }

    @Override
    protected Class<FoodScan> getResponseType() {
        return FoodScan.class;
    }

    @Override
    protected Resource getSystemPrompt() {
        return this.systemPrompt;
    }
}
