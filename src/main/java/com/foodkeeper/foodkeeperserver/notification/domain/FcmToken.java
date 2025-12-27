package com.foodkeeper.foodkeeperserver.notification.domain;

import java.time.LocalDateTime;

public record FcmToken(Long id,
                       String fcmToken,
                       LocalDateTime createdAt,
                       LocalDateTime updatedAt,
                       String memberKey) {
    public static FcmToken create(String fcmToken, String memberKey) {
        return new FcmToken(null, fcmToken, null, null, memberKey);
    }
}
