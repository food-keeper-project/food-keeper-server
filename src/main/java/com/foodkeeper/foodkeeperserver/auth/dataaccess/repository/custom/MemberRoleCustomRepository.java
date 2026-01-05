package com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.MemberRoleEntity;

import java.util.List;

public interface MemberRoleCustomRepository {

    List<MemberRoleEntity> findByMemberKey(String memberKey);
}
