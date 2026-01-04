package com.foodkeeper.foodkeeperserver.notification.domain;

public record FcmMessage(String fcmToken, String title, String foodName, Long remainingDays, String type) {
}
