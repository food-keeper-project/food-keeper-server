package com.foodkeeper.foodkeeperserver.auth.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.OauthEntity;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.custom.OauthCustomRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

@NullMarked
public interface OauthRepository extends JpaRepository<OauthEntity, Long>, OauthCustomRepository {
}
