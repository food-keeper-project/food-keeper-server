package com.foodkeeper.foodkeeperserver.notification.controller.v1.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshFcmTokenRequest(@NotBlank String fcmToken) {
}
