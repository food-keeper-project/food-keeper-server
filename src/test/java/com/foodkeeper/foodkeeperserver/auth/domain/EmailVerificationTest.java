package com.foodkeeper.foodkeeperserver.auth.domain;

import com.foodkeeper.foodkeeperserver.auth.domain.enums.EmailVerificationStatus;
import com.foodkeeper.foodkeeperserver.member.domain.Email;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class EmailVerificationTest {

    @Test
    @DisplayName("verified 상태이면 true를 반환한다.")
    void returnTrueIfVerified() {
        EmailVerification emailVerification = EmailVerification.builder()
                .emailCode(new EmailCode(new Email("test@mail.com"), "123456"))
                .failedCount(1)
                .expiredAt(LocalDateTime.now().plusMinutes(5))
                .status(EmailVerificationStatus.VERIFIED)
                .build();

        assertThat(emailVerification.isVerified()).isTrue();
    }

    @Test
    @DisplayName("verified 상태가 아니면 false를 반환한다.")
    void returnFalseIfNotVerified() {
        EmailVerification emailVerification = EmailVerification.builder()
                .emailCode(new EmailCode(new Email("test@mail.com"), "123456"))
                .failedCount(1)
                .expiredAt(LocalDateTime.now().plusMinutes(5))
                .status(EmailVerificationStatus.ACTIVE)
                .build();

        assertThat(emailVerification.isVerified()).isFalse();
    }

    @Test
    @DisplayName("expired 상태이면 true를 반환한다.")
    void returnTrueIfExpired() {
        EmailVerification emailVerification = EmailVerification.builder()
                .emailCode(new EmailCode(new Email("test@mail.com"), "123456"))
                .failedCount(1)
                .expiredAt(LocalDateTime.now().plusMinutes(5))
                .status(EmailVerificationStatus.EXPIRED)
                .build();

        assertThat(emailVerification.isExpired()).isTrue();
    }

    @Test
    @DisplayName("expired 상태가 아니면 false를 반환한다.")
    void returnFalseIfNotExpired() {
        EmailVerification emailVerification = EmailVerification.builder()
                .emailCode(new EmailCode(new Email("test@mail.com"), "123456"))
                .failedCount(1)
                .expiredAt(LocalDateTime.now().plusMinutes(5))
                .status(EmailVerificationStatus.ACTIVE)
                .build();

        assertThat(emailVerification.isExpired()).isFalse();
    }

    @Test
    @DisplayName("expiredAt보다 이후면 true를 반환한다.")
    void returnTrueIfAfterExpiredAt() {
        EmailVerification emailVerification = EmailVerification.builder()
                .emailCode(new EmailCode(new Email("test@mail.com"), "123456"))
                .failedCount(1)
                .expiredAt(LocalDateTime.now().minusMinutes(2))
                .status(EmailVerificationStatus.ACTIVE)
                .build();

        assertThat(emailVerification.isExpired()).isTrue();
    }

    @Test
    @DisplayName("expiredAt보다 이전이면 false를 반환한다.")
    void returnFalseIfBeforeExpiredAt() {
        EmailVerification emailVerification = EmailVerification.builder()
                .emailCode(new EmailCode(new Email("test@mail.com"), "123456"))
                .failedCount(1)
                .expiredAt(LocalDateTime.now().plusMinutes(5))
                .status(EmailVerificationStatus.ACTIVE)
                .build();

        assertThat(emailVerification.isExpired()).isFalse();
    }

    @Test
    @DisplayName("failedCount가 기준 이상이면 true를 반환한다.")
    void returnTrueIfFailedCountExceededStand() {
        EmailVerification emailVerification = EmailVerification.builder()
                .emailCode(new EmailCode(new Email("test@mail.com"), "123456"))
                .failedCount(5)
                .expiredAt(LocalDateTime.now().plusMinutes(5))
                .status(EmailVerificationStatus.ACTIVE)
                .build();

        assertThat(emailVerification.isFailedExceeded(5)).isTrue();
    }

    @Test
    @DisplayName("failedCount가 기준 이상이면 false를 반환한다.")
    void returnFalseIfFailedCountUnderStand() {
        EmailVerification emailVerification = EmailVerification.builder()
                .emailCode(new EmailCode(new Email("test@mail.com"), "123456"))
                .failedCount(4)
                .expiredAt(LocalDateTime.now().plusMinutes(5))
                .status(EmailVerificationStatus.ACTIVE)
                .build();

        assertThat(emailVerification.isFailedExceeded(5)).isFalse();
    }
}