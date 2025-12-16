package com.foodkeeper.foodkeeperserver.auth.implement;

import com.foodkeeper.foodkeeperserver.auth.domain.KakaoUser;
import com.foodkeeper.foodkeeperserver.auth.domain.OAuthUser;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
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

    public OAuthUser authenticate(String accessToken) {
        KakaoUser kakaoUser = restClient.get().uri(KAKAO_USER_INFO_URL)
                .header(HttpHeaders.AUTHORIZATION, BEARER + accessToken)
                .retrieve()
                .body(KakaoUser.class);

        if (kakaoUser == null) {
            throw new AppException(ErrorType.INVALID_OAUTH_USER);
        }

        return kakaoUser.toOAuthUser();
    }
}
