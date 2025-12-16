package com.foodkeeper.foodkeeperserver.auth.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.foodkeeper.foodkeeperserver.member.domain.enums.OAuthProvider;

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
