package com.foodkeeper.foodkeeperserver.member.implement;

import com.foodkeeper.foodkeeperserver.member.domain.KakaoUser;
import com.foodkeeper.foodkeeperserver.member.domain.OAuthMember;
import com.foodkeeper.foodkeeperserver.member.domain.enums.OAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class KakaoAuthenticator implements OAuthAuthenticator {

    private static final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";
    private static final String BEARER = "Bearer ";

    private final RestClient restClient;

    public OAuthMember authenticate(String accessToken) {
        KakaoUser kakaoUser = restClient.get().uri(KAKAO_USER_INFO_URL)
                .header(HttpHeaders.AUTHORIZATION, BEARER + accessToken)
                .retrieve()
                .body(KakaoUser.class);

        return OAuthMember.builder()
                .account(kakaoUser.id().toString())
                .provider(OAuthProvider.KAKAO)
                .nickname(kakaoUser.kakaoAccount().profile().nickname())
                .email(kakaoUser.kakaoAccount().email())
                .profileImageUrl(kakaoUser.kakaoAccount().profile().profileImageUrl())
                .build();
    }
}
