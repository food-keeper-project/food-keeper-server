package com.foodkeeper.foodkeeperserver.clova.controller.response;

import com.foodkeeper.foodkeeperserver.clova.domain.AiType;

public record Message(AiType aiType, String content) {
}
