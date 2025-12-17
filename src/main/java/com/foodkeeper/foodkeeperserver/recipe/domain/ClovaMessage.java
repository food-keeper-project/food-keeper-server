package com.foodkeeper.foodkeeperserver.recipe.domain;

public record ClovaMessage(AiType aiType, String content) {
    public static ClovaMessage toRequest(AiType aiType, String content) {
        return new ClovaMessage(aiType, content);
    }
}
