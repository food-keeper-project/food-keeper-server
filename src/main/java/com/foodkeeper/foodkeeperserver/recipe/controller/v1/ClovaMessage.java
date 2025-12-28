package com.foodkeeper.foodkeeperserver.recipe.controller.v1;

import com.foodkeeper.foodkeeperserver.recipe.domain.AiType;

public record ClovaMessage(AiType role, String content) {
    public static ClovaMessage toRequest(AiType role, String content) {
        return new ClovaMessage(role, content);
    }
}
