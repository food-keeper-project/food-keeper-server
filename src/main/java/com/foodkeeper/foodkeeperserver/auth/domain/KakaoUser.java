package com.foodkeeper.foodkeeperserver.auth.domain;

import com.foodkeeper.foodkeeperserver.member.domain.Email;
import com.foodkeeper.foodkeeperserver.member.domain.Nickname;
import com.foodkeeper.foodkeeperserver.member.domain.ProfileImageUrl;
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
                .nickname(new Nickname(kakaoAccount.profile().nickname()))
                .email(new Email(kakaoAccount.email()))
                .profileImageUrl(new ProfileImageUrl(kakaoAccount.profile().profileImageUrl()))
                .build();
    }
}
