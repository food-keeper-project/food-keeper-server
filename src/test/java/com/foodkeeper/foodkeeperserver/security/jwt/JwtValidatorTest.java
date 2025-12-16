package com.foodkeeper.foodkeeperserver.security.jwt;

import com.foodkeeper.foodkeeperserver.auth.implement.JwtValidator;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtValidatorTest {

    SecretKey secretKey = Keys.hmacShaKeyFor("this_is_a_test_secret_key_abcdefghijtlmnopqr".getBytes(StandardCharsets.UTF_8));
    JwtValidator jwtValidator = new JwtValidator(secretKey);

    @Test
    @DisplayName("토큰이 만료되면 AppException이 발생한다.")
    void throwAppExceptionExceptionIfTokenIsExpired() {
        String memberKey = "memberKey";
        String token =
                Jwts.builder()
                        .subject(memberKey)
                        .expiration(Date.from(Instant.now().minus(1L, ChronoUnit.MILLIS)))
                        .signWith(secretKey)
                        .compact();

        assertThatThrownBy(() -> jwtValidator.getSubjectIfValid(token))
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.EXPIRED_JWT);
    }

    @Test
    @DisplayName("JWT가 손상되면 AppException이 발생한다.")
    void throwAppExceptionIfTokenIsMalformed() {
        String memberKey = "memberKey";
        String token =
                Jwts.builder()
                        .subject(memberKey)
                        .expiration(Date.from(Instant.now().plus(1000 * 60 * 60, ChronoUnit.MILLIS)))
                        .signWith(secretKey)
                        .compact()
                        .substring(1);

        assertThatThrownBy(() -> jwtValidator.validate(token))
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.MALFORMED_JWT);
    }

    @Test
    @DisplayName("Signature가 맞지 않으면 AppException이 발생한다.")
    void throwAppExceptionIfSignatureIsInvalid() {
        String signature = "1signaturesignaturesignaturesignature";
        SecretKey invalidSecretKey = Keys.hmacShaKeyFor(signature.getBytes(StandardCharsets.UTF_8));
        String memberKey = "memberKey";
        String token =
                Jwts.builder()
                        .subject(memberKey)
                        .expiration(Date.from(Instant.now().plus(1000 * 60 * 60, ChronoUnit.MILLIS)))
                        .signWith(invalidSecretKey)
                        .compact();

        assertThatThrownBy(() -> jwtValidator.validate(token))
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.INVALID_SIGNATURE);
    }

    @Test
    @DisplayName("지원하지 않는 JWT 형태이면 AppException이 발생한다.")
    void throwAppExceptionExceptionIfJwtIsUnsupported() {
        String memberKey = "memberKey";
        String token =
                Jwts.builder()
                        .subject(memberKey)
                        .expiration(Date.from(Instant.now().plus(1000 * 60 * 60, ChronoUnit.MILLIS)))
                        .compact();

        assertThatThrownBy(() -> jwtValidator.validate(token))
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.UNSUPPORTED_JWT);
    }

    @Test
    @DisplayName("JWT 검증에 성공하면 subject를 반환한다.")
    void getMemberIdIfJwtIsInvalid() {
        String memberKey = "memberKey";
        String token =
                Jwts.builder()
                        .subject(memberKey)
                        .expiration(Date.from(Instant.now().plus(1000 * 60 * 60, ChronoUnit.MILLIS)))
                        .signWith(secretKey)
                        .compact();

        String sub = jwtValidator.getSubjectIfValid(token);

        assertThat(sub).isEqualTo(memberKey);
    }

    @Test
    @DisplayName("JWT 검증에 성공해도 멤버 ID가 JWT에 들어있지 않으면 AppException이 발생한다.")
    void throwAppExceptionIfNotExistsMemberIdInJwt() {
        String token =
                Jwts.builder()
                        .expiration(Date.from(Instant.now().plus(1000 * 60 * 60, ChronoUnit.MILLIS)))
                        .signWith(secretKey)
                        .compact();

        assertThatThrownBy(() -> jwtValidator.getSubjectIfValid(token))
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.NOT_FOUND_SUBJECT);
    }
}