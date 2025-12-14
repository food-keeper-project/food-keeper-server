package com.foodkeeper.foodkeeperserver.auth.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.SignInLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignInLogRepository extends JpaRepository<SignInLogEntity, Long> {
}
