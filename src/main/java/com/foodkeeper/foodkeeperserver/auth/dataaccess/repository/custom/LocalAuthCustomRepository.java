package com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.LocalAuthEntity;

import java.util.Optional;

public interface LocalAuthCustomRepository {

    boolean existsByAccount(String account);

    boolean existsByEmail(String email);

    boolean existsByEmailAndAccount(String email, String account);

    Optional<LocalAuthEntity> findByAccountAndPassword(String account, String encodedPassword);

    Optional<LocalAuthEntity> findByEmail(String email);

    Optional<LocalAuthEntity> findByEmailAndAccount(String email, String account);
}
