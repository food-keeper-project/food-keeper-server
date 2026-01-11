package com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.LocalAuthEntity;

import java.util.List;
import java.util.Optional;

public interface LocalAuthCustomRepository {

    boolean existsByAccount(String account);

    boolean existsByEmail(String email);

    boolean existsByEmailAndAccount(String email, String account);

    Optional<LocalAuthEntity> findByAccount(String account);

    Optional<LocalAuthEntity> findByEmail(String email);

    Optional<LocalAuthEntity> findByEmailAndAccount(String email, String account);

    List<LocalAuthEntity> findAllByMemberKey(String memberKey);
}
