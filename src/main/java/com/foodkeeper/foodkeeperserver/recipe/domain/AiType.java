package com.foodkeeper.foodkeeperserver.recipe.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AiType {
    SYSTEM("system", "역할을 규정하는 지시문"),
    USER("user", "재료"),
    ASSISTANT("assistant", "AI 답변");

    private final String value;
    private final String description;

    @JsonValue
    public String getValue() {
        return value;
    }
}
