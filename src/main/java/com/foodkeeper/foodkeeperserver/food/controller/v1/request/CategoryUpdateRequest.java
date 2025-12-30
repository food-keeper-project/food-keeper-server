package com.foodkeeper.foodkeeperserver.food.controller.v1.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryUpdateRequest(@NotBlank @Size(min = 1, max = 15) String name) {
}
