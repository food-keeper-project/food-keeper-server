package com.foodkeeper.foodkeeperserver.notification.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.notification.dataaccess.entity.FcmTokenEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FcmTokenCustomRepository {

    void deleteFcmTokens(String memberKey);

    List<FcmTokenEntity> findAllByMemberKeyIn(Set<String> memberKeys);

    Optional<FcmTokenEntity> findByToken(String token);

    void deleteByToken(String token);
}
