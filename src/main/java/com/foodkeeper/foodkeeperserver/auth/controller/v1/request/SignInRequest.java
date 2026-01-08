package com.foodkeeper.foodkeeperserver.auth.controller.v1.request;

import com.foodkeeper.foodkeeperserver.auth.domain.SignInContext;
import com.foodkeeper.foodkeeperserver.member.domain.IpAddress;
import com.foodkeeper.foodkeeperserver.auth.domain.enums.OAuthProvider;
import jakarta.validation.constraints.NotBlank;

public record SignInRequest(@NotBlank String accessToken, @NotBlank String fcmToken) {

    public SignInContext toContext(OAuthProvider provider, String ipAddress) {
        return SignInContext.builder()
                .accessToken(accessToken)
                .oAuthProvider(provider)
                .fcmToken(fcmToken)
                .ipAddress(new IpAddress(ipAddress))
                .build();
    }
}
