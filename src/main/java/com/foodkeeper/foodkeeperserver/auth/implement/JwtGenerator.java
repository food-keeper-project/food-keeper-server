package com.foodkeeper.foodkeeperserver.auth.implement;

import com.foodkeeper.foodkeeperserver.auth.domain.Jwt;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class JwtGenerator {

    private static final Long ACCESS_TOKEN_VALIDATION_MILLIS = 1000L * 60 * 30;
    private static final Long REFRESH_TOKEN_VALIDATION_MILLIS = 1000L * 60 * 60 * 24 * 30;

    private final SecretKey secretKey;

    public Jwt generateJwt(String memberKey) {
        if (memberKey == null || memberKey.isBlank()) {
            throw new AppException(ErrorType.INVALID_MEMBER_KEY);
        }
        return new Jwt(generateAccessToken(memberKey), generateRefreshToken(memberKey));
    }

    private String generateAccessToken(String memberKey) {
        return buildToken(memberKey, ACCESS_TOKEN_VALIDATION_MILLIS);
    }

    private String generateRefreshToken(String memberKey) {
        return buildToken(memberKey, REFRESH_TOKEN_VALIDATION_MILLIS);
    }

    private String buildToken(String memberKey, long expireMillis) {
        return Jwts.builder()
                .subject(memberKey)
                .expiration(Date.from(Instant.now().plus(expireMillis, ChronoUnit.MILLIS)))
                .signWith(secretKey)
                .compact();
    }
}