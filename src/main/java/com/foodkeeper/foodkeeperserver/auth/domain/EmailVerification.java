package com.foodkeeper.foodkeeperserver.auth.domain;

import com.foodkeeper.foodkeeperserver.auth.domain.enums.EmailVerificationStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EmailVerification(EmailCode emailCode,
                                Integer failedCount,
                                LocalDateTime expiredAt,
                                EmailVerificationStatus status) {

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
}
