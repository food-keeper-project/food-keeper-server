package com.foodkeeper.foodkeeperserver.auth.implement;

import com.foodkeeper.foodkeeperserver.auth.domain.Jwt;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class JwtGeneratorTest {

    SecretKey secretKey;
    JwtGenerator jwtGenerator;

    @BeforeEach
    void setUp() {
        secretKey = Keys.hmacShaKeyFor("this_is_a_test_secret_key_abcdefghijtlmnopqr".getBytes(StandardCharsets.UTF_8));
        jwtGenerator = new JwtGenerator(secretKey);
    }

    @Test
    @DisplayName("JWT를 생성한다.")
    void generateJwt() {
        // given
        String memberKey = "memberKey";

        // when
        Jwt jwt = jwtGenerator.generateJwt(memberKey);

        // then
        assertThat(jwt.accessToken()).isNotBlank();
        assertThat(jwt.refreshToken()).isNotBlank();
        assertThat(jwt.accessToken()).isNotEqualTo(jwt.refreshToken());
    }

    @Test
    @DisplayName("accessToken과 refreshToken의 subject는 memberKey이다.")
    void accessTokenAndRefreshTokenSubjectIsMemberKey() {
        // given
        String memberKey = "memberKey";

        // when
        Jwt jwt = jwtGenerator.generateJwt(memberKey);

        // then
        assertThat(parse(jwt.accessToken()).getSubject()).isEqualTo(memberKey);
        assertThat(parse(jwt.refreshToken()).getSubject()).isEqualTo(memberKey);
    }

    @Test
    @DisplayName("accessToken과 refreshToken의 만료 시간이 다르다.")
    void accessTokenAndRefreshTokenExpiresAtDifferent() {
        // given
        String memberKey = "memberKey";
        Instant now = Instant.now();

        // when
        Jwt jwt = jwtGenerator.generateJwt(memberKey);

        // then
        assertThat(parse(jwt.accessToken()).getExpiration())
                .isAfter(now)
                .isBefore(now.plus(31, ChronoUnit.MINUTES));
        assertThat(parse(jwt.refreshToken()).getExpiration())
                .isAfter(now.plus(29, ChronoUnit.DAYS))
                .isBefore(now.plus(31, ChronoUnit.DAYS));
    }

    @Test
    @DisplayName("memberKey가 null이면 AppException이 발생한다.")
    void throwAppExceptionIfMemberKeyIsNull() {
        assertThatThrownBy(() -> jwtGenerator.generateJwt(null))
                .isInstanceOf(AppException.class);
    }

    @Test
    @DisplayName("memberKey가 blank이면 AppException이 발생한다.")
    void throwAppExceptionIfMemberKeyIsBlank() {
        assertThatThrownBy(() -> jwtGenerator.generateJwt(" "))
                .isInstanceOf(AppException.class);
    }

    private Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}