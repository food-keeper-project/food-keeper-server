package com.foodkeeper.foodkeeperserver.auth.e2e;

import com.foodkeeper.foodkeeperserver.auth.controller.v1.response.AuthTokenResponse;
import com.foodkeeper.foodkeeperserver.auth.domain.OAuthUser;
import com.foodkeeper.foodkeeperserver.auth.implement.JwtValidator;
import com.foodkeeper.foodkeeperserver.auth.implement.OAuthAuthenticator;
import com.foodkeeper.foodkeeperserver.member.dataaccess.entity.MemberEntity;
import com.foodkeeper.foodkeeperserver.member.dataaccess.repository.MemberRepository;
import com.foodkeeper.foodkeeperserver.member.domain.Email;
import com.foodkeeper.foodkeeperserver.member.domain.Nickname;
import com.foodkeeper.foodkeeperserver.member.domain.ProfileImageUrl;
import com.foodkeeper.foodkeeperserver.member.domain.enums.OAuthProvider;
import com.foodkeeper.foodkeeperserver.member.fixture.MemberEntityFixture;
import com.foodkeeper.foodkeeperserver.notification.dataaccess.entity.FcmTokenEntity;
import com.foodkeeper.foodkeeperserver.notification.dataaccess.repository.FcmRepository;
import com.foodkeeper.foodkeeperserver.support.integration.E2ETest;
import com.foodkeeper.foodkeeperserver.support.response.ApiResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

public class AuthE2ETest extends E2ETest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    FcmRepository fcmRepository;
    @Autowired
    JwtValidator jwtValidator;
    @MockitoBean
    OAuthAuthenticator oAuthAuthenticator;

    @Test
    @DisplayName("카카오 계정을 통한 OAuth 로그인을 한다.")
    void signInWithKakao() {
        ParameterizedTypeReference<ApiResponse<AuthTokenResponse>> responseType =
                new ParameterizedTypeReference<>() {};
        OAuthUser oAuthUser = new OAuthUser("account",
                OAuthProvider.KAKAO,
                new Nickname("nickname"),
                new Email("test@mail.com"),
                new ProfileImageUrl("https://test.com/image.jpg"));
        given(oAuthAuthenticator.authenticate(anyString())).willReturn(oAuthUser);
        String signInRequest = """
                {
                    "accessToken": "kakaoAccessToken",
                    "fcmToken": "fcmToken"
                }
                """;

        ApiResponse<AuthTokenResponse> response = client.post()
                .uri("/api/v1/auth/sign-in/kakao")
                .contentType(MediaType.APPLICATION_JSON)
                .body(signInRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(responseType)
                .returnResult()
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.data()).isNotNull();

        String memberKey = jwtValidator.getSubjectIfValid(response.data().accessToken());
        MemberEntity member = memberRepository.findByMemberKey(memberKey).orElse(null);
        List<FcmTokenEntity> fcmTokens = fcmRepository.findAllByMemberKeyIn(Set.of(memberKey));

        assertThat(member).isNotNull();
        assertThat(member.getEmail()).isEqualTo("test@mail.com");
        assertThat(fcmTokens).hasSize(1);
        assertThat(fcmTokens.getFirst().getToken()).isEqualTo("fcmToken");
    }

    @Test
    @DisplayName("자신의 프로필을 조회한다.")
    void getMyProfile() {
        MemberEntity member = memberRepository.save(MemberEntityFixture.DEFAULT.get());

        client.get()
                .uri("/api/v1/members/me")
                .header(AUTHORIZATION, getAccessToken(member.getMemberKey()))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.nickname").isEqualTo(member.getNickname())
                .jsonPath("$.data.imageUrl").isEqualTo(member.getImageUrl());
    }
}
