package com.foodkeeper.foodkeeperserver.recipe.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AiType {
    SYSTEM("역할을 규정하는 지시문"),
    USER("재료"),
    ASSISTANT("AI 답변");

    private final String description;
}
