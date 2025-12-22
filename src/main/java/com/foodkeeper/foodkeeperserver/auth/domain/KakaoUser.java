package com.foodkeeper.foodkeeperserver.auth.domain;

import com.foodkeeper.foodkeeperserver.member.domain.enums.OAuthProvider;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoUser(Long id, LocalDateTime connectedAt, KakaoAccount kakaoAccount) {

    public OAuthUser toOAuthUser() {
        return OAuthUser.builder()
                .account(id.toString())
                .provider(OAuthProvider.KAKAO)
                .nickname(kakaoAccount.profile().nickname())
                .email(kakaoAccount.email())
                .profileImageUrl(kakaoAccount.profile().profileImageUrl())
                .build();
    }
}
