package com.foodkeeper.foodkeeperserver.auth.business;

import com.foodkeeper.foodkeeperserver.auth.domain.EmailCode;
import com.foodkeeper.foodkeeperserver.auth.domain.EmailVerification;
import com.foodkeeper.foodkeeperserver.auth.domain.SignUpContext;
import com.foodkeeper.foodkeeperserver.auth.implement.*;
import com.foodkeeper.foodkeeperserver.member.domain.Email;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LocalAuthService {
    private final LocalAuthAuthenticator localAuthAuthenticator;
    private final LocalAuthFinder localAuthFinder;
    private final LocalAuthRegistrar localAuthRegistrar;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificator emailVerificator;
    private final RefreshTokenManager refreshTokenManager;
    private final LocalAuthLockManager lockManager;

    public void signUp(SignUpContext context) {
        try {
            int lockTimeOut = 3;
            lockManager.acquire(context.getEmail(), lockTimeOut);
            localAuthRegistrar.register(context.toNewLocalMember(passwordEncoder.encode(context.getPassword())));
        } finally {
            lockManager.release(context.getEmail());
        }
    }

    public boolean isDuplicatedAccount(String account) {
        return localAuthAuthenticator.isDuplicatedAccount(account);
    }

    public void verifyEmail(Email email) {
        if (localAuthFinder.existsEmail(email)) {
            throw new AppException(ErrorType.DUPLICATED_EMAIL);
        }

        emailVerificator.sendVerificationCode(email, LocalDateTime.now().plusMinutes(5));
    }

    public void verifyEmailCode(EmailCode emailCode) {
        EmailVerification emailVerification = emailVerificator.findEmailVerification(emailCode.email());

        if (emailVerification.isVerified()) {
            throw new AppException(ErrorType.INVALID_EMAIL_CODE);
        }

        if (emailVerification.isNotEqualsCode(emailCode.code())) {
            emailVerificator.incrementFailCount(emailCode.email());
            throw new AppException(ErrorType.INVALID_EMAIL_CODE);
        }

        if (emailVerification.isFailedExceeded(5)) {
            emailVerification.block();
            emailVerificator.updateVerification(emailVerification);
            throw new AppException(ErrorType.TOO_MUCH_FAILED);
        }

        if (emailVerification.isExpired()) {
            emailVerification.expire();
            emailVerificator.updateVerification(emailVerification);
            throw new AppException(ErrorType.EXPIRED_EMAIL_CODE);
        }

        emailVerification.verify();
        emailVerificator.updateVerification(emailVerification);
    }

    public void signOut(String memberKey) {
        refreshTokenManager.remove(memberKey);
    }
}
