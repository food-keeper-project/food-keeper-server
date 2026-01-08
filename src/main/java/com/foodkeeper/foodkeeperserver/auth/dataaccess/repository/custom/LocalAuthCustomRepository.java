package com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.LocalAuthEntity;

import java.util.Optional;

public interface LocalAuthCustomRepository {

    boolean existsByAccount(String account);

    boolean existsEmail(String email);

    Optional<LocalAuthEntity> findByAccountAndPassword(String account, String encodedPassword);
}
