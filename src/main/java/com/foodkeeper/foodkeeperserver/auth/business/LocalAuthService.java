package com.foodkeeper.foodkeeperserver.auth.business;

import com.foodkeeper.foodkeeperserver.auth.domain.*;
import com.foodkeeper.foodkeeperserver.auth.enums.VerificationMessageTitle;
import com.foodkeeper.foodkeeperserver.auth.implement.*;
import com.foodkeeper.foodkeeperserver.member.domain.Email;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LocalAuthService {
    private final LocalAuthAuthenticator localAuthAuthenticator;
    private final LocalAuthFinder localAuthFinder;
    private final LocalAuthRegistrar localAuthRegistrar;
    private final EmailVerificator emailVerificator;
    private final JwtGenerator jwtGenerator;
    private final LocalAuthRecoverer localAuthRecoverer;
    private final LocalAuthLockManager lockManager;
    private final ApplicationEventPublisher eventPublisher;

    public void signUp(SignUpContext context) {
        try {
            int lockTimeOut = 3;
            lockManager.acquire(context.getEmail(), lockTimeOut);
            EncodedPassword encodedPassword = localAuthAuthenticator.encodePassword(context.password());
            localAuthRegistrar.register(context.toNewLocalMember(encodedPassword));
        } finally {
            lockManager.release(context.getEmail());
        }
    }

    public Jwt signIn(LocalSignInContext context) {
        String memberKey = localAuthAuthenticator.authenticate(context.account(), context.password());

        Jwt jwt = jwtGenerator.generateJwt(memberKey);

        eventPublisher.publishEvent(
                new SignInEvent(context.getIpAddress(), jwt.refreshToken(), context.fcmToken(), memberKey));

        return jwt;
    }

    public boolean isDuplicatedAccount(LocalAccount account) {
        return localAuthAuthenticator.isDuplicatedAccount(account);
    }

    public void verifyEmailForRecoverAccount(Email email) {
        if (!localAuthFinder.existsEmail(email)) {
            throw new AppException(ErrorType.INVALID_EMAIL);
        }

        emailVerificator.sendVerificationCode(email, LocalDateTime.now().plusMinutes(5),
                VerificationMessageTitle.RECOVER_ACCOUNT.getTitle());
    }

    public void verifyEmailForRecoverPassword(Email email, LocalAccount account) {
        if (!localAuthFinder.existsEmailAndAccount(email, account)) {
            throw new AppException(ErrorType.NOT_FOUND_ACCOUNT);
        }

        emailVerificator.sendVerificationCode(email, LocalDateTime.now().plusMinutes(5),
                VerificationMessageTitle.RECOVER_PASSWORD.getTitle());
    }

    public void verifyEmailForSignUp(Email email) {
        if (localAuthFinder.existsEmail(email)) {
            throw new AppException(ErrorType.DUPLICATED_EMAIL);
        }

        emailVerificator.sendVerificationCode(email, LocalDateTime.now().plusMinutes(5),
                VerificationMessageTitle.SIGN_UP.getTitle());
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

    public void recoverAccount(EmailCode emailCode) {
        verifyEmailCode(emailCode);

        localAuthRecoverer.recoverAccount(emailCode.email());
    }

    public void findPassword(EmailCode emailCode, LocalAccount account) {
        verifyEmailCode(emailCode);

        if (!localAuthFinder.existsEmailAndAccount(emailCode.email(), account)) {
            throw new AppException(ErrorType.NOT_FOUND_ACCOUNT);
        }
    }

    public void changePassword(Email email, LocalAccount account, Password password) {
        EmailVerification emailVerification = emailVerificator.findEmailVerification(email);
        if (!emailVerification.isVerified()) {
            throw new AppException(ErrorType.NOT_VERIFIED_EMAIL);
        }

        localAuthRecoverer.changePassword(email, account, password);
    }

    public void signOut(String memberKey) {
        eventPublisher.publishEvent(new SignOutEvent(memberKey));
    }
}
