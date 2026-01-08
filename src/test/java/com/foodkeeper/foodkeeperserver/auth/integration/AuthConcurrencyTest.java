package com.foodkeeper.foodkeeperserver.auth.integration;

import com.foodkeeper.foodkeeperserver.auth.business.LocalAuthService;
import com.foodkeeper.foodkeeperserver.auth.business.OauthService;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.EmailVerificationEntity;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.LocalAuthEntity;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.OauthEntity;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.EmailVerificationRepository;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.LocalAuthRepository;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.OauthRepository;
import com.foodkeeper.foodkeeperserver.auth.domain.OAuthUser;
import com.foodkeeper.foodkeeperserver.auth.domain.OauthSignInContext;
import com.foodkeeper.foodkeeperserver.auth.domain.SignUpContext;
import com.foodkeeper.foodkeeperserver.auth.domain.enums.EmailVerificationStatus;
import com.foodkeeper.foodkeeperserver.auth.domain.enums.OAuthProvider;
import com.foodkeeper.foodkeeperserver.auth.implement.OAuthAuthenticator;
import com.foodkeeper.foodkeeperserver.member.domain.Email;
import com.foodkeeper.foodkeeperserver.member.domain.IpAddress;
import com.foodkeeper.foodkeeperserver.member.domain.Nickname;
import com.foodkeeper.foodkeeperserver.member.domain.ProfileImageUrl;
import com.foodkeeper.foodkeeperserver.member.domain.enums.Gender;
import com.foodkeeper.foodkeeperserver.support.integration.SpringTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AuthConcurrencyTest extends SpringTest {

    @MockitoBean OAuthAuthenticator oAuthAuthenticator;
    @Autowired OauthService oauthService;
    @Autowired OauthRepository oauthRepository;
    @Autowired LocalAuthService localAuthService;
    @Autowired LocalAuthRepository localAuthRepository;
    @Autowired EmailVerificationRepository emailVerificationRepository;

    @Test
    @DisplayName("같은 OAuth 계정이 동시에 로그인 시도 시 1명만 가입 가능하다.")
    void onlyOneMemberCanSignInWithOAuth() throws InterruptedException {
        int count = 100;
        ExecutorService executor = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(count);
        String accessToken = "access";
        String account = "account";
        OAuthUser oAuthUser = OAuthUser.builder()
                .account(account)
                .provider(OAuthProvider.KAKAO)
                .nickname(new Nickname("nickname"))
                .email(new Email("test@mail.com"))
                .profileImageUrl(ProfileImageUrl.empty())
                .build();
        given(oAuthAuthenticator.authenticate(eq(accessToken))).willReturn(oAuthUser);

        for (int i = 0; i < count; i++) {
            executor.submit(() -> {
                try {
                    IpAddress ip = new IpAddress("127.0.0.1");
                    oauthService.signInByOAuth(new OauthSignInContext(accessToken, OAuthProvider.KAKAO, "fcm", ip));
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        List<OauthEntity> oauthEntities = oauthRepository.findAll();

        assertThat(oauthEntities).hasSize(1);
    }

    @Test
    @DisplayName("같은 로컬 계정이 동시에 로그인 시도 시 1명만 가입 가능하다.")
    void onlyOneMemberCanSignInWithLocalAuth() throws InterruptedException {
        // given
        int count = 100;
        String email = "test@mail.com";
        String code = "123456";
        EmailVerificationEntity emailVerification = emailVerificationRepository.save(
                new EmailVerificationEntity(email, code, LocalDateTime.now().plusMinutes(5)));
        emailVerification.update(EmailVerificationStatus.VERIFIED);
        emailVerificationRepository.save(emailVerification);
        ExecutorService executor = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(count);
        System.out.println(emailVerification.getEmail() + " : " + emailVerification.getVerificationStatus());

        // when
        for (int i = 0; i < count; i++) {
            executor.submit(() -> {
                try {
                    localAuthService.signUp(SignUpContext.builder()
                            .account("account123")
                            .password("password123")
                            .email(email)
                            .nickname("nickname")
                            .gender(Gender.M)
                            .ipAddress("127.0.0.1")
                            .build());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        List<LocalAuthEntity> localAuthEntities = localAuthRepository.findAll();

        assertThat(localAuthEntities).hasSize(1);
    }
}
