package com.foodkeeper.foodkeeperserver.auth.domain;

import com.foodkeeper.foodkeeperserver.auth.domain.enums.EmailVerificationStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class EmailVerification {
    private Long id;
    private EmailCode emailCode;
    private Integer failedCount;
    private LocalDateTime expiredAt;
    private EmailVerificationStatus status;

    public boolean isFailedExceeded(Integer stand) {
        return failedCount >= stand;
    }

    public boolean isNotEqualsCode(String code) {
        return !emailCode.code().equals(code);
    }

    public boolean isVerified() {
        return status.equals(EmailVerificationStatus.VERIFIED);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiredAt) || status.equals(EmailVerificationStatus.EXPIRED);
    }

    public boolean isBlocked() {
        return status.equals(EmailVerificationStatus.BLOCKED);
    }

    public void expire() {
        this.status = EmailVerificationStatus.EXPIRED;
    }

    public void verify() {
        this.status = EmailVerificationStatus.VERIFIED;
    }

    public void block() {
        this.status = EmailVerificationStatus.BLOCKED;
    }
}
