package com.foodkeeper.foodkeeperserver.auth.domain;

import com.foodkeeper.foodkeeperserver.member.domain.enums.OAuthProvider;
import lombok.Builder;

@Builder
public record MemberRegister(String accessToken,
                             OAuthProvider oAuthProvider,
                             String fcmToken,
                             String ipAddress) {
}
