package com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.EmailVerificationEntity;

import java.util.Optional;

public interface EmailVerificationCustomRepository {

    void updateVerificationsStatusToExpired(String email);

    Optional<EmailVerificationEntity> findByEmail(String email);

    void incrementFailedCount(String email);
}
