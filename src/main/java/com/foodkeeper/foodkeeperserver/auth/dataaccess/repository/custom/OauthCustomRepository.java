package com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.OauthEntity;

import java.util.List;
import java.util.Optional;

public interface OauthCustomRepository {

    Optional<OauthEntity> findByAccount(String account);

    List<OauthEntity> findAllByMemberKey(String memberKey);
}
