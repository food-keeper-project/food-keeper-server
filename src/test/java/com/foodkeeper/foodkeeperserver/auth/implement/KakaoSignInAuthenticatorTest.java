package com.foodkeeper.foodkeeperserver.auth.implement;

import com.foodkeeper.foodkeeperserver.auth.domain.OAuthMember;
import com.foodkeeper.foodkeeperserver.member.domain.enums.OAuthProvider;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ExtendWith(MockitoExtension.class)
class KakaoSignInAuthenticatorTest {

    MockRestServiceServer mockServer;
    KakaoAuthenticator kakaoAuthenticator;

    @BeforeEach
    void setUp() {
        RestClient.Builder restClientBuilder = RestClient.builder();
        // 순서 주의: mockserver가 먼저 와야 함
        mockServer = MockRestServiceServer.bindTo(restClientBuilder).build();
        RestClient restClient = restClientBuilder.build();
        kakaoAuthenticator = new KakaoAuthenticator(restClient);
    }

    @Test
    @DisplayName("accessToken에 해당하는 유저를 조회하여 인증된 정보를 가져온다.")
    void getKakaoUserByAccessToken() {
        // given
        String accessToken = "accessToken";
        String kakaoResponse = """
                {
                  "id": 123456789,
                  "kakaoAccount": {
                    "email": "test@kakao.com",
                    "profile": {
                      "nickname": "kakao",
                      "profileImageUrl": "https://image.kakao.com/profile.jpg"
                    }
                  }
                }
                """;
        mockServer.expect(requestTo("https://kapi.kakao.com/v2/user/me"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andRespond(withSuccess(kakaoResponse, MediaType.APPLICATION_JSON));

        // when
        OAuthMember oauthMember = kakaoAuthenticator.authenticate(accessToken);

        // then
        assertThat(oauthMember.account()).isEqualTo("123456789");
        assertThat(oauthMember.provider()).isEqualTo(OAuthProvider.KAKAO);
        assertThat(oauthMember.email()).isEqualTo("test@kakao.com");
        assertThat(oauthMember.nickname()).isEqualTo("kakao");
        assertThat(oauthMember.profileImageUrl())
                .isEqualTo("https://image.kakao.com/profile.jpg");
    }

    @Test
    @DisplayName("응답이 null일 경우 AppException이 발생한다.")
    void throwAppExceptionIfResponseIsNull() {
        // given
        mockServer.expect(requestTo("https://kapi.kakao.com/v2/user/me"))
                .andRespond(withSuccess("", MediaType.APPLICATION_JSON));

        // then
        assertThatThrownBy(() -> kakaoAuthenticator.authenticate("accessToken"))
                .isInstanceOf(AppException.class);
    }

    @Test
    @DisplayName("응답이 unauthorized일 경우 RestClientResponseException이 발생한다.")
    void throwRestClientResponseExceptionIfResponseIsUnauthorized() {
        // given
        mockServer.expect(requestTo("https://kapi.kakao.com/v2/user/me"))
                .andRespond(withStatus(HttpStatus.UNAUTHORIZED));

        // then
        assertThatThrownBy(() -> kakaoAuthenticator.authenticate("invalid-token"))
                .isInstanceOf(RestClientResponseException.class);
    }
}