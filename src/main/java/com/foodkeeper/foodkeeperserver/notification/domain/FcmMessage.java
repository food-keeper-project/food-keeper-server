package com.foodkeeper.foodkeeperserver.notification.domain;

public record FcmMessage(String fcmToken, String title, String body, String priority) {
}
