package com.foodkeeper.foodkeeperserver.auth.controller.v1.request;

import com.foodkeeper.foodkeeperserver.auth.domain.OauthSignInContext;
import com.foodkeeper.foodkeeperserver.member.domain.IpAddress;
import com.foodkeeper.foodkeeperserver.auth.domain.enums.OAuthProvider;
import jakarta.validation.constraints.NotBlank;

public record OauthSignInRequest(@NotBlank String accessToken, @NotBlank String fcmToken) {

    public OauthSignInContext toContext(OAuthProvider provider, String ipAddress) {
        return OauthSignInContext.builder()
                .accessToken(accessToken)
                .oAuthProvider(provider)
                .fcmToken(fcmToken)
                .ipAddress(new IpAddress(ipAddress))
                .build();
    }
}
