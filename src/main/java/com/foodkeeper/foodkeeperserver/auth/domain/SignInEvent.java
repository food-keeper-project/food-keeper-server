package com.foodkeeper.foodkeeperserver.auth.domain;

public record SignInEvent(String ipAddress,
                          String refreshToken,
                          String fcmToken,
                          String memberKey) {
}
