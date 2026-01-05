package com.foodkeeper.foodkeeperserver.auth.business;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.EmailVerificationRepository;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.LocalAuthRepository;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.MemberRoleRepository;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.OauthRepository;
import com.foodkeeper.foodkeeperserver.auth.implement.EmailVerificator;
import com.foodkeeper.foodkeeperserver.auth.implement.LocalAuthAuthenticator;
import com.foodkeeper.foodkeeperserver.auth.implement.RefreshTokenManager;
import com.foodkeeper.foodkeeperserver.common.handler.TransactionHandler;
import com.foodkeeper.foodkeeperserver.food.implement.CategoryManager;
import com.foodkeeper.foodkeeperserver.mail.service.AppMailSender;
import com.foodkeeper.foodkeeperserver.member.dataaccess.repository.MemberRepository;
import com.foodkeeper.foodkeeperserver.member.implement.MemberFinder;
import com.foodkeeper.foodkeeperserver.member.implement.MemberRegistrar;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class LocalOauthServiceTest {
    @Mock MemberRepository memberRepository;
    @Mock OauthRepository oauthRepository;
    @Mock MemberRoleRepository memberRoleRepository;
    @Mock CategoryManager foodCategoryManager;
    @Mock EmailVerificationRepository emailVerificationRepository;
    @Mock TransactionHandler transactionHandler;
    @Mock JavaMailSender javaMailSender;
    @Mock PasswordEncoder passwordEncoder;
    @Mock LocalAuthRepository localAuthRepository;
    SecretKey secretKey;
    LocalAuthService localAuthService;

    @BeforeEach
    void setUp() {
        secretKey = Keys.hmacShaKeyFor("this_is_a_test_secret_key_abcdefghijtlmnopqr".getBytes(StandardCharsets.UTF_8));
        MemberFinder memberFinder = new MemberFinder(memberRepository);
        MemberRegistrar memberRegistrar = new MemberRegistrar(memberRepository, oauthRepository, localAuthRepository,
                memberRoleRepository, foodCategoryManager);
        LocalAuthAuthenticator localAuthAuthenticator = new LocalAuthAuthenticator(localAuthRepository, memberRepository);
        AppMailSender appMailSender = new AppMailSender(javaMailSender);
        EmailVerificator emailVerificator = new EmailVerificator(emailVerificationRepository, appMailSender, transactionHandler);
        RefreshTokenManager refreshTokenManager = new RefreshTokenManager(memberRepository);
        localAuthService = new LocalAuthService(localAuthAuthenticator, memberFinder, memberRegistrar, passwordEncoder,
                emailVerificator, refreshTokenManager);
    }

    @Test
    @DisplayName("계정이 중복된다면 true가 반환된다.")
    void returnTrueIfAccountIsDuplicated() {
        // given
        String account = "account";
        given(localAuthRepository.existsByAccount(eq(account))).willReturn(true);

        boolean isDuplicated = localAuthService.isDuplicatedAccount(account);

        assertThat(isDuplicated).isTrue();
    }
}