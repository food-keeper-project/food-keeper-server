package com.foodkeeper.foodkeeperserver.auth.business;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.EmailVerificationEntity;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.LocalAuthEntity;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.EmailVerificationRepository;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.LocalAuthRepository;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.MemberRoleRepository;
import com.foodkeeper.foodkeeperserver.auth.domain.*;
import com.foodkeeper.foodkeeperserver.auth.domain.enums.EmailVerificationStatus;
import com.foodkeeper.foodkeeperserver.auth.implement.*;
import com.foodkeeper.foodkeeperserver.common.handler.TransactionHandler;
import com.foodkeeper.foodkeeperserver.food.implement.CategoryManager;
import com.foodkeeper.foodkeeperserver.mail.implement.AppMailSender;
import com.foodkeeper.foodkeeperserver.member.dataaccess.repository.MemberRepository;
import com.foodkeeper.foodkeeperserver.member.domain.Email;
import com.foodkeeper.foodkeeperserver.member.domain.IpAddress;
import com.foodkeeper.foodkeeperserver.member.implement.MemberRegistrar;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocalAuthServiceTest {
    @Mock MemberRepository memberRepository;
    @Mock MemberRoleRepository memberRoleRepository;
    @Mock EmailVerificationRepository emailVerificationRepository;
    @Mock LocalAuthRepository localAuthRepository;
    @Mock CategoryManager foodCategoryManager;
    @Mock TransactionHandler transactionHandler;
    @Mock JavaMailSender javaMailSender;
    @Mock PasswordEncoder passwordEncoder;
    @Mock ApplicationEventPublisher eventPublisher;
    JwtGenerator jwtGenerator;
    SecretKey secretKey;
    LocalAuthService localAuthService;

    @BeforeEach
    void setUp() {
        secretKey = Keys.hmacShaKeyFor("this_is_a_test_secret_key_abcdefghijtlmnopqr".getBytes(StandardCharsets.UTF_8));
        LocalAuthFinder localAuthFinder = new LocalAuthFinder(localAuthRepository);
        MemberRegistrar memberRegistrar = new MemberRegistrar(memberRepository, memberRoleRepository, foodCategoryManager);
        LocalAuthAuthenticator localAuthAuthenticator = new LocalAuthAuthenticator(localAuthRepository, passwordEncoder);
        AppMailSender appMailSender = new AppMailSender(javaMailSender);
        EmailVerificator emailVerificator = new EmailVerificator(emailVerificationRepository, appMailSender, transactionHandler);
        RefreshTokenManager refreshTokenManager = new RefreshTokenManager(memberRepository);
        LocalAuthLockManager lockManager = new LocalAuthLockManager(localAuthRepository);
        LocalAuthRegistrar localAuthRegistrar = new LocalAuthRegistrar(localAuthRepository, memberRegistrar, emailVerificator);
        jwtGenerator = new JwtGenerator(secretKey);
        localAuthService = new LocalAuthService(localAuthAuthenticator, localAuthFinder, localAuthRegistrar,
                emailVerificator, refreshTokenManager, jwtGenerator, lockManager, eventPublisher);
    }

    @Test
    @DisplayName("계정이 중복된다면 true가 반환된다.")
    void returnTrueIfAccountIsDuplicated() {
        // given
        String account = "account";
        given(localAuthRepository.existsByAccount(eq(account))).willReturn(true);

        // when
        boolean isDuplicated = localAuthService.isDuplicatedAccount(account);

        // then
        assertThat(isDuplicated).isTrue();
    }

    @Test
    @DisplayName("이미 존재하는 이메일이면 AppExcpetion이 발생한다.")
    void throwAppExceptionIfEmailExists() {
        // given
        String email = "test@mail.com";
        given(localAuthRepository.existsEmail(eq(email))).willReturn(true);

        // then
        assertThatCode(() -> localAuthService.verifyEmail(new Email(email)))
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.DUPLICATED_EMAIL);
    }

    @Test
    @DisplayName("존재하지 않는 이메일이면 코드가 발송된다.")
    void sendVerificationCodeIfEmailNotExists() {
        // given
        String email = "test@mail.com";
        given(localAuthRepository.existsEmail(eq(email))).willReturn(false);

        // when
        localAuthService.verifyEmail(new Email(email));

        // then
        verify(transactionHandler, times(1)).afterCommit(any());
    }

    @Test
    @DisplayName("failedCount가 5 이상이면 AppException이 발생한다.")
    void throwAppExceptionIfFailedCountExceededFive() {
        // given
        String email = "test@mail.com";
        String code = "123456";
        EmailVerificationEntity emailVerificationEntity = mock(EmailVerificationEntity.class);
        EmailVerification emailVerification = EmailVerification.builder()
                .emailCode(EmailCode.of(email, code))
                .failedCount(5)
                .expiredAt(LocalDateTime.now().plusMinutes(5))
                .status(EmailVerificationStatus.ACTIVE)
                .build();
        given(emailVerificationRepository.findByEmail(eq(email))).willReturn(Optional.of(emailVerificationEntity));
        given(emailVerificationRepository.findById(any())).willReturn(Optional.of(emailVerificationEntity));
        given(emailVerificationEntity.toDomain()).willReturn(emailVerification);

        // then
        assertThatCode(() -> localAuthService.verifyEmailCode(new EmailCode(new Email(email), code)))
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.TOO_MUCH_FAILED);
    }

    @Test
    @DisplayName("code가 일치하지 않으면 AppException이 발생한다.")
    void throwAppExceptionIfNotEqualsCode() {
        // given
        String email = "test@mail.com";
        String code = "123456";
        EmailVerificationEntity emailVerificationEntity = mock(EmailVerificationEntity.class);
        EmailVerification emailVerification = EmailVerification.builder()
                .emailCode(EmailCode.of(email, code))
                .failedCount(1)
                .expiredAt(LocalDateTime.now().plusMinutes(5))
                .status(EmailVerificationStatus.ACTIVE)
                .build();
        given(emailVerificationRepository.findByEmail(eq(email))).willReturn(Optional.of(emailVerificationEntity));
        given(emailVerificationEntity.toDomain()).willReturn(emailVerification);

        // then
        assertThatCode(() -> localAuthService.verifyEmailCode(new EmailCode(new Email(email), "654321")))
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.INVALID_EMAIL_CODE);
    }

    @Test
    @DisplayName("이미 verified 상태면 AppException이 발생한다.")
    void throwAppExceptionIfAlreadyVerified() {
        // given
        String email = "test@mail.com";
        String code = "123456";
        EmailVerificationEntity emailVerificationEntity = mock(EmailVerificationEntity.class);
        EmailVerification emailVerification = EmailVerification.builder()
                .emailCode(EmailCode.of(email, code))
                .failedCount(1)
                .expiredAt(LocalDateTime.now().plusMinutes(5))
                .status(EmailVerificationStatus.VERIFIED)
                .build();
        given(emailVerificationRepository.findByEmail(eq(email))).willReturn(Optional.of(emailVerificationEntity));
        given(emailVerificationEntity.toDomain()).willReturn(emailVerification);

        // then
        assertThatCode(() -> localAuthService.verifyEmailCode(new EmailCode(new Email(email), "123456")))
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.INVALID_EMAIL_CODE);
    }

    @Test
    @DisplayName("expired 상태면 AppException이 발생한다.")
    void throwAppExceptionIfExpired() {
        // given
        String email = "test@mail.com";
        String code = "123456";
        EmailVerificationEntity emailVerificationEntity = mock(EmailVerificationEntity.class);
        EmailVerification emailVerification = EmailVerification.builder()
                .emailCode(EmailCode.of(email, code))
                .failedCount(1)
                .expiredAt(LocalDateTime.now().plusMinutes(5))
                .status(EmailVerificationStatus.EXPIRED)
                .build();
        given(emailVerificationRepository.findByEmail(eq(email))).willReturn(Optional.of(emailVerificationEntity));
        given(emailVerificationRepository.findById(any())).willReturn(Optional.of(emailVerificationEntity));
        given(emailVerificationEntity.toDomain()).willReturn(emailVerification);

        // then
        assertThatCode(() -> localAuthService.verifyEmailCode(new EmailCode(new Email(email), "123456")))
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.EXPIRED_EMAIL_CODE);
    }

    @Test
    @DisplayName("조건을 만족하면 인증이 성공한다.")
    void successEmailVerificationWhenConditionAllTrue() {
        // given
        String email = "test@mail.com";
        String code = "123456";
        EmailVerificationEntity emailVerificationEntity = mock(EmailVerificationEntity.class);
        EmailVerification emailVerification = EmailVerification.builder()
                .emailCode(EmailCode.of(email, code))
                .failedCount(1)
                .expiredAt(LocalDateTime.now().plusMinutes(5))
                .status(EmailVerificationStatus.ACTIVE)
                .build();
        given(emailVerificationRepository.findByEmail(eq(email))).willReturn(Optional.of(emailVerificationEntity));
        given(emailVerificationRepository.findById(any())).willReturn(Optional.of(emailVerificationEntity));
        given(emailVerificationEntity.toDomain()).willReturn(emailVerification);

        // then
        assertThatCode(() -> localAuthService.verifyEmailCode(new EmailCode(new Email(email), "123456")))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("account와 password가 일치하면 로그인이 성공한다.")
    void successSignInIfAccountAndPasswordAreValid() {
        // given
        String account = "account123";
        String password = "password123";
        String encodedPassword = "encodedPassword";
        String memberKey = "memberKey";
        LocalAuthEntity localAuthEntity = new LocalAuthEntity(account, encodedPassword, memberKey);
        LocalSignInContext context =
                new LocalSignInContext(new LocalAccount(account), new Password(password), "fcm", new IpAddress("127.0.0.1"));
        given(passwordEncoder.encode(eq(password))).willReturn(encodedPassword);
        given(localAuthRepository.findByAccountAndPassword(eq(account), eq(encodedPassword)))
                .willReturn(Optional.of(localAuthEntity));

        // when
        Jwt jwt = localAuthService.signIn(context);

        // then
        Jwt generatedJwt = jwtGenerator.generateJwt(memberKey);
        assertThat(jwt.accessToken()).isEqualTo(generatedJwt.accessToken());
        assertThat(jwt.refreshToken()).isEqualTo(generatedJwt.refreshToken());
    }
}