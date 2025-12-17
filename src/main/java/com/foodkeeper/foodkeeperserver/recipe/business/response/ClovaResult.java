package com.foodkeeper.foodkeeperserver.recipe.business.response;

import com.foodkeeper.foodkeeperserver.recipe.domain.ClovaMessage;

public record ClovaResult(ClovaMessage message, Integer created, String finishReason) {
}
