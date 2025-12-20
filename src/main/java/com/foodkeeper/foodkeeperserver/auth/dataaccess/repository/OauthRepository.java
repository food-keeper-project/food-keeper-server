package com.foodkeeper.foodkeeperserver.auth.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.OauthEntity;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@NullMarked
public interface OauthRepository extends JpaRepository<OauthEntity, Long> {

    boolean existsByAccount(String account);

    Optional<OauthEntity> findByAccount(String account);
}
