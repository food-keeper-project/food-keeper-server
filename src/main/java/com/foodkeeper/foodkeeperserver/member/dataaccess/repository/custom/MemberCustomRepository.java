package com.foodkeeper.foodkeeperserver.member.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.member.dataaccess.entity.MemberEntity;

import java.util.Optional;

public interface MemberCustomRepository {
    Optional<MemberEntity> findByMemberKey(String memberKey);

    Optional<String> findRefreshToken(String memberKey);

    void updateRefreshToken(String memberKey, String refreshToken);

    void deleteRefreshTokenByMemberKey(String memberKey);

    boolean existsByEmail(String email);
}
