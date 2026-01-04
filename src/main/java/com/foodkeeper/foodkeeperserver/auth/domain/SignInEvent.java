package com.foodkeeper.foodkeeperserver.auth.domain;

public record SignInEvent(String ipAddress,
                          String fcmToken,
                          String memberKey) {
}
