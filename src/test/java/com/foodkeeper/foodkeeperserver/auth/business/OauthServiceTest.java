package com.foodkeeper.foodkeeperserver.auth.business;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.OauthEntity;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.LocalAuthRepository;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.MemberRoleRepository;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.OauthRepository;
import com.foodkeeper.foodkeeperserver.auth.domain.Jwt;
import com.foodkeeper.foodkeeperserver.auth.domain.OAuthUser;
import com.foodkeeper.foodkeeperserver.auth.domain.SignInContext;
import com.foodkeeper.foodkeeperserver.auth.implement.JwtGenerator;
import com.foodkeeper.foodkeeperserver.auth.implement.KakaoAuthenticator;
import com.foodkeeper.foodkeeperserver.auth.implement.OauthFinder;
import com.foodkeeper.foodkeeperserver.auth.implement.OauthLockManager;
import com.foodkeeper.foodkeeperserver.food.implement.CategoryManager;
import com.foodkeeper.foodkeeperserver.member.dataaccess.entity.MemberEntity;
import com.foodkeeper.foodkeeperserver.member.dataaccess.repository.MemberRepository;
import com.foodkeeper.foodkeeperserver.member.domain.Email;
import com.foodkeeper.foodkeeperserver.member.domain.IpAddress;
import com.foodkeeper.foodkeeperserver.member.domain.Nickname;
import com.foodkeeper.foodkeeperserver.member.domain.ProfileImageUrl;
import com.foodkeeper.foodkeeperserver.auth.domain.enums.OAuthProvider;
import com.foodkeeper.foodkeeperserver.member.implement.MemberRegistrar;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class OauthServiceTest {

    @Mock MemberRepository memberRepository;
    @Mock OauthRepository oauthRepository;
    @Mock MemberRoleRepository memberRoleRepository;
    @Mock KakaoAuthenticator kakaoAuthenticator;
    @Mock CategoryManager foodCategoryManager;
    @Mock LocalAuthRepository localAuthRepository;
    @Mock ApplicationEventPublisher eventPublisher;
    SecretKey secretKey;
    OauthService oauthService;

    @BeforeEach
    void setUp() {
        secretKey = Keys.hmacShaKeyFor("this_is_a_test_secret_key_abcdefghijtlmnopqr".getBytes(StandardCharsets.UTF_8));
        JwtGenerator jwtGenerator = new JwtGenerator(secretKey);
        OauthFinder oauthFinder = new OauthFinder(oauthRepository);
        MemberRegistrar memberRegistrar = new MemberRegistrar(memberRepository, oauthRepository, localAuthRepository,
                memberRoleRepository, foodCategoryManager);
        OauthLockManager oauthLockManager = new OauthLockManager(oauthRepository);
        oauthService = new OauthService(kakaoAuthenticator, oauthFinder, memberRegistrar, jwtGenerator, eventPublisher,
                oauthLockManager);
    }

    @Test
    @DisplayName("기존 OAuth 회원이면 memberKey를 조회해 JWT를 발급한다.")
    void issueJwtIfOAuthMemberExist() {
        // given
        String account = "account";
        String accessToken = "accessToken";
        String memberKey = "memberKey";
        OAuthProvider provider = OAuthProvider.KAKAO;
        SignInContext register = SignInContext.builder()
                .accessToken(accessToken)
                .ipAddress(new IpAddress("127.0.0.1"))
                .fcmToken("fcmToken")
                .oAuthProvider(provider)
                .build();
        OAuthUser oauthUser = OAuthUser.builder()
                .account(account)
                .email(new Email("email@test.com"))
                .nickname(new Nickname("nickname"))
                .profileImageUrl(new ProfileImageUrl("https://test.com/image.jpg"))
                .build();
        OauthEntity oauthEntity = new OauthEntity(provider, account, memberKey);
        given(kakaoAuthenticator.authenticate(eq(accessToken))).willReturn(oauthUser);
        given(oauthRepository.findByEmail(eq(account), eq(provider))).willReturn(Optional.of(oauthEntity));

        // when
        Jwt jwt = oauthService.signInByOAuth(register);

        // then
        assertThat(jwt.accessToken()).isNotBlank();
        assertThat(jwt.refreshToken()).isNotBlank();
        assertThat(jwt.accessToken()).isNotEqualTo(jwt.refreshToken());
    }

    @Test
    @DisplayName("신규 OAuth 회원이면 회원가입 후 JWT를 발급한다.")
    void issueJwtIfOAuthMemberNotExistAfterRegister() {
        // given
        String account = "account";
        String accessToken = "accessToken";
        String memberKey = "memberKey";
        SignInContext register = SignInContext.builder()
                .accessToken(accessToken)
                .ipAddress(new IpAddress("127.0.0.1"))
                .fcmToken("fcmToken")
                .oAuthProvider(OAuthProvider.KAKAO)
                .build();
        OAuthUser oauthUser = OAuthUser.builder()
                .account(account)
                .provider(OAuthProvider.KAKAO)
                .email(new Email("email@test.com"))
                .nickname(new Nickname("nickname"))
                .profileImageUrl(new ProfileImageUrl("https://test.com/image.jpg"))
                .build();
        OauthEntity oauthEntity = new OauthEntity(OAuthProvider.KAKAO, account, memberKey);
        MemberEntity memberEntity = mock(MemberEntity.class);
        given(memberEntity.getMemberKey()).willReturn(memberKey);
        given(kakaoAuthenticator.authenticate(eq(accessToken))).willReturn(oauthUser);
        given(memberRepository.save(any(MemberEntity.class))).willReturn(memberEntity);
        given(oauthRepository.save(any(OauthEntity.class))).willReturn(oauthEntity);

        // when
        Jwt jwt = oauthService.signInByOAuth(register);

        // then
        assertThat(jwt.accessToken()).isNotBlank();
        assertThat(jwt.refreshToken()).isNotBlank();
        assertThat(jwt.accessToken()).isNotEqualTo(jwt.refreshToken());
    }
}