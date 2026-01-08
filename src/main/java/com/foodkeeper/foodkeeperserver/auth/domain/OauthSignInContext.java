package com.foodkeeper.foodkeeperserver.auth.domain;

import com.foodkeeper.foodkeeperserver.member.domain.IpAddress;
import com.foodkeeper.foodkeeperserver.auth.domain.enums.OAuthProvider;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import lombok.Builder;

@Builder
public record OauthSignInContext(String accessToken,
                                 OAuthProvider oAuthProvider,
                                 String fcmToken,
                                 IpAddress ipAddress) {

    public OauthSignInContext {
        if (accessToken == null) {
            throw new AppException(ErrorType.OAUTH_ACCESS_TOKEN_IS_NULL);
        }

        if (fcmToken == null) {
            throw new AppException(ErrorType.FCM_TOKEN_IS_NULL);
        }

        if (oAuthProvider == null) {
            throw new AppException(ErrorType.DEFAULT_ERROR);
        }
    }

    public String getIpAddress() {
        return ipAddress.ipAddress();
    }
}
