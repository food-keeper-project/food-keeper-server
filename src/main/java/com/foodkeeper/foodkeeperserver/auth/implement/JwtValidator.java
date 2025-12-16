package com.foodkeeper.foodkeeperserver.auth.implement;

import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@RequiredArgsConstructor
public class JwtValidator {

    private final SecretKey secretKey;

    public String getSubjectIfValid(String token) {
        String subject = validate(token).getPayload().getSubject();

        if (subject == null) {
            throw new AppException(ErrorType.NOT_FOUND_SUBJECT);
        }

        return subject;
    }

    public Jws<Claims> validate(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
        } catch (MalformedJwtException e) {
            throw new AppException(ErrorType.MALFORMED_JWT);
        } catch (UnsupportedJwtException e) {
            throw new AppException(ErrorType.UNSUPPORTED_JWT);
        } catch (ExpiredJwtException e) {
            throw new AppException(ErrorType.EXPIRED_JWT);
        } catch (SecurityException | SignatureException e) {
            throw new AppException(ErrorType.INVALID_SIGNATURE);
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorType.INVALID_JWT);
        }
    }
}
