package com.foodkeeper.foodkeeperserver.notification.domain;

import java.time.LocalDateTime;

public record FcmToken(Long id,
                       String token,
                       LocalDateTime createdAt,
                       LocalDateTime updatedAt,
                       String memberKey) {
    public static FcmToken create(String token, String memberKey) {
        return new FcmToken(null, token, null, null, memberKey);
    }
}
