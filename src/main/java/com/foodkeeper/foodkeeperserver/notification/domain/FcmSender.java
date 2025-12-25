package com.foodkeeper.foodkeeperserver.notification.domain;

public record FcmSender(String fcmToken, String title, String body) {
}
