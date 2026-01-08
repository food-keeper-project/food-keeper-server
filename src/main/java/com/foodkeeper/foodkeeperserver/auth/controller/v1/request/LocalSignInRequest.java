package com.foodkeeper.foodkeeperserver.auth.controller.v1.request;

import com.foodkeeper.foodkeeperserver.auth.controller.v1.validation.ValidAccount;
import com.foodkeeper.foodkeeperserver.auth.controller.v1.validation.ValidPassword;
import com.foodkeeper.foodkeeperserver.auth.domain.LocalSignInContext;

public record LocalSignInRequest(@ValidAccount String account,
                                 @ValidPassword String password,
                                 String fcmToken) {

    public LocalSignInContext toContext(String ipAddress) {
        return LocalSignInContext.of(account, password, fcmToken, ipAddress);
    }
}
