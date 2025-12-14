package com.foodkeeper.foodkeeperserver.member.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.member.dataaccess.entity.SignInEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignInRepository extends JpaRepository<SignInEntity, Long> {
}
