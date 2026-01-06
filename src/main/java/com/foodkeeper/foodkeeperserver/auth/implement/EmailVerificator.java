package com.foodkeeper.foodkeeperserver.auth.implement;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.EmailVerificationEntity;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.EmailVerificationRepository;
import com.foodkeeper.foodkeeperserver.auth.domain.EmailVerification;
import com.foodkeeper.foodkeeperserver.auth.domain.EmailCode;
import com.foodkeeper.foodkeeperserver.common.handler.TransactionHandler;
import com.foodkeeper.foodkeeperserver.mail.service.AppMailSender;
import com.foodkeeper.foodkeeperserver.member.domain.Email;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class EmailVerificator {
    private static final String VERIFICATION_MESSAGE_TITLE = "[키친로그] 회원가입 인증 번호입니다.";
    private static final String VERIFICATION_MESSAGE_BODY = "[인증 번호]\n%s";
    private final EmailVerificationRepository emailVerificationRepository;
    private final AppMailSender appMailSender;
    private final TransactionHandler transactionHandler;

    @Transactional
    public void sendVerificationCode(Email email, LocalDateTime expiredAt) {
        String code = generateVerificationCode();

        emailVerificationRepository.updateVerificationsStatusToExpired(email.email());
        emailVerificationRepository.save(new EmailVerificationEntity(email.email(), code, expiredAt));

        transactionHandler.afterCommit(() ->
                appMailSender.send(email.email(), VERIFICATION_MESSAGE_TITLE, VERIFICATION_MESSAGE_BODY.formatted(code)));
    }

    private String generateVerificationCode() {
        return String.valueOf(new SecureRandom().nextInt(900000) + 100000);
    }

    public EmailVerification findEmailVerification(Email email) {
        return emailVerificationRepository.findByEmail(email.email())
                .orElseThrow(() -> new AppException(ErrorType.INVALID_EMAIL_CODE)).toDomain();
    }

    @Transactional
    public void incrementFailCount(Email email) {
        emailVerificationRepository.incrementFailedCount(email.email());
    }

    @Transactional
    public void expireCode(EmailCode emailCode) {
        emailVerificationRepository.updateStatusToExpired(emailCode.getEmail(), emailCode.code());
    }

    @Transactional
    public void makeAsVerified(EmailCode emailCode) {
        emailVerificationRepository.updateStatusToVerified(emailCode.getEmail(), emailCode.code());
    }

    public boolean isVerified(Email email) {
        return findEmailVerification(email).isVerified();
    }
}
