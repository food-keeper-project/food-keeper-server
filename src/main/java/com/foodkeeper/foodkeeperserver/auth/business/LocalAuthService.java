package com.foodkeeper.foodkeeperserver.auth.business;

import com.foodkeeper.foodkeeperserver.auth.domain.EmailCode;
import com.foodkeeper.foodkeeperserver.auth.domain.EmailVerification;
import com.foodkeeper.foodkeeperserver.auth.domain.SignUpContext;
import com.foodkeeper.foodkeeperserver.auth.implement.EmailVerificator;
import com.foodkeeper.foodkeeperserver.auth.implement.LocalAuthAuthenticator;
import com.foodkeeper.foodkeeperserver.auth.implement.RefreshTokenManager;
import com.foodkeeper.foodkeeperserver.member.domain.Email;
import com.foodkeeper.foodkeeperserver.member.implement.MemberFinder;
import com.foodkeeper.foodkeeperserver.member.implement.MemberRegistrar;
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
    private final MemberFinder memberFinder;
    private final MemberRegistrar memberRegistrar;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificator emailVerificator;
    private final RefreshTokenManager refreshTokenManager;

    public void signUp(SignUpContext context) {
        if (!emailVerificator.isVerified(context.email())) {
            throw new AppException(ErrorType.NOT_VERIFIED_EMAIL);
        }
        memberRegistrar.register(context.toNewLocalMember(passwordEncoder.encode(context.getPassword())));
    }

    public boolean isDuplicatedAccount(String account) {
        return localAuthAuthenticator.isDuplicatedAccount(account);
    }

    public void verifyEmail(Email email) {
        if (memberFinder.existsEmail(email)) {
            throw new AppException(ErrorType.INVALID_EMAIL);
        }

        emailVerificator.sendVerificationCode(email, LocalDateTime.now().plusMinutes(5));
    }

    public void verifyEmailCode(EmailCode emailCode) {
        EmailVerification emailVerification = emailVerificator.findEmailVerification(emailCode.email());

        if (emailVerification.isFailedExceeded(5)) {
            throw new AppException(ErrorType.TOO_MUCH_FAILED);
        }

        if (emailVerification.isNotEqualsCode(emailCode.code())) {
            emailVerificator.incrementFailCount(emailCode.email());
            throw new AppException(ErrorType.INVALID_EMAIL_CODE);
        }

        if (emailVerification.isVerified()) {
            throw new AppException(ErrorType.INVALID_EMAIL_CODE);
        }

        if (emailVerification.isExpired()) {
            emailVerificator.expireCode(emailCode);
            throw new AppException(ErrorType.EXPIRED_EMAIL_CODE);
        }

        emailVerificator.makeAsVerified(emailCode);
    }

    public void signOut(String memberKey) {
        refreshTokenManager.remove(memberKey);
    }
}
