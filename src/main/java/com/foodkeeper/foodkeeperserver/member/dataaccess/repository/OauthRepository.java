package com.foodkeeper.foodkeeperserver.member.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.member.dataaccess.entity.OauthEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OauthRepository extends JpaRepository<OauthEntity, Long> {

    boolean existsByAccount(String account);

    Optional<OauthEntity> findByAccount(String account);
}
