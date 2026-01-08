package com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.OauthEntity;
import com.foodkeeper.foodkeeperserver.auth.domain.enums.OAuthProvider;

import java.util.List;
import java.util.Optional;

public interface OauthCustomRepository {

    Optional<OauthEntity> findByEmail(String email, OAuthProvider provider);

    List<OauthEntity> findAllByMemberKey(String memberKey);
}
