package com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.MemberRoleEntity;
import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.enums.EntityStatus;
import com.foodkeeper.foodkeeperserver.support.repository.QuerydslRepositorySupport;

import java.util.List;

import static com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.QMemberRoleEntity.memberRoleEntity;

public class MemberRoleCustomRepositoryImpl extends QuerydslRepositorySupport implements MemberRoleCustomRepository {

    protected MemberRoleCustomRepositoryImpl() {
        super(MemberRoleEntity.class);
    }

    @Override
    public List<MemberRoleEntity> findByMemberKey(String memberKey) {
        return selectFrom(memberRoleEntity)
                .where(memberRoleEntity.memberKey.eq(memberKey), memberRoleEntity.status.eq(EntityStatus.ACTIVE))
                .fetch();
    }
}
