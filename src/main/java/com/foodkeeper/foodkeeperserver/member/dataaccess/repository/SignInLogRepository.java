package com.foodkeeper.foodkeeperserver.member.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.member.dataaccess.entity.SignInLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignInLogRepository extends JpaRepository<SignInLogEntity, Long> {
}
