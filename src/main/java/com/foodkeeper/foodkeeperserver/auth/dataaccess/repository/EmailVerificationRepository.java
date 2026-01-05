package com.foodkeeper.foodkeeperserver.auth.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.EmailVerificationEntity;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.custom.EmailVerificationCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationRepository extends JpaRepository<EmailVerificationEntity, Long>,
        EmailVerificationCustomRepository {
}
