package com.foodkeeper.foodkeeperserver.auth.business;

import com.foodkeeper.foodkeeperserver.auth.domain.Jwt;
import com.foodkeeper.foodkeeperserver.auth.implement.JwtGenerator;
import com.foodkeeper.foodkeeperserver.auth.implement.JwtValidator;
import com.foodkeeper.foodkeeperserver.auth.implement.RefreshTokenManager;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenRefreshService {

    private final JwtValidator jwtValidator;
    private final JwtGenerator jwtGenerator;
    private final RefreshTokenManager refreshTokenManager;

    public Jwt refresh(String refreshToken, String memberKey) {
        jwtValidator.validate(refreshToken);

        String savedRefreshToken = refreshTokenManager.find(memberKey);
        if (refreshToken.equals(savedRefreshToken)) {
            Jwt jwt = jwtGenerator.generateJwt(memberKey);
            refreshTokenManager.updateRefreshToken(memberKey, jwt.refreshToken());
            return jwt;
        }

        throw new AppException(ErrorType.FAILED_AUTH);
    }
}
