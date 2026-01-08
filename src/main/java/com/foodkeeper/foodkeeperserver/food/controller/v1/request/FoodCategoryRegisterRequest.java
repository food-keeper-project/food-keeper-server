package com.foodkeeper.foodkeeperserver.food.controller.v1.request;

import jakarta.validation.constraints.NotBlank;

public record FoodCategoryRegisterRequest(@NotBlank String name) {
}
