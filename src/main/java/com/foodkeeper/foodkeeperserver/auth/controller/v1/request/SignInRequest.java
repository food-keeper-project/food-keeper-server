package com.foodkeeper.foodkeeperserver.auth.controller.v1.request;

import jakarta.validation.constraints.NotBlank;

public record SignInRequest(@NotBlank String accessToken, @NotBlank String fcmToken) {
}
