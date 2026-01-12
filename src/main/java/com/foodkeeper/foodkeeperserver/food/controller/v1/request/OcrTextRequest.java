package com.foodkeeper.foodkeeperserver.food.controller.v1.request;

import jakarta.validation.constraints.NotNull;

public record OcrTextRequest(@NotNull String ocrText) {
}
