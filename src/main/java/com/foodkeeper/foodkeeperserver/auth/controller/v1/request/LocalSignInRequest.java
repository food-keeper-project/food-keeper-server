package com.foodkeeper.foodkeeperserver.auth.controller.v1.request;

import com.foodkeeper.foodkeeperserver.auth.domain.LocalSignInContext;

public record LocalSignInRequest(String account,
                                 String password,
                                 String fcmToken) {

    public LocalSignInContext toContext(String ipAddress) {
        return LocalSignInContext.of(account, password, fcmToken, ipAddress);
    }
}
