package com.foodkeeper.foodkeeperserver.ai.domain;

public record ClovaMessage(AiType role, String content) {
    public static ClovaMessage toRequest(AiType role, String content) {
        return new ClovaMessage(role, content);
    }
}
