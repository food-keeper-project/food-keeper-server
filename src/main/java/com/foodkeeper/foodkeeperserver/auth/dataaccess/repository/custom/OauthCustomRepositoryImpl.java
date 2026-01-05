package com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.OauthEntity;
import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.enums.EntityStatus;
import com.foodkeeper.foodkeeperserver.support.repository.QuerydslRepositorySupport;

import java.util.List;
import java.util.Optional;

import static com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.QOauthEntity.oauthEntity;

public class OauthCustomRepositoryImpl extends QuerydslRepositorySupport implements OauthCustomRepository {

    protected OauthCustomRepositoryImpl() {
        super(OauthEntity.class);
    }

    @Override
    public Optional<OauthEntity> findByAccount(String account) {
        return Optional.ofNullable(
                selectFrom(oauthEntity)
                        .where(oauthEntity.account.eq(account), oauthEntity.status.ne(EntityStatus.DELETED))
                        .fetchOne()
        );
    }

    @Override
    public List<OauthEntity> findAllByMemberKey(String memberKey) {
        return selectFrom(oauthEntity)
                .where(oauthEntity.memberKey.eq(memberKey), oauthEntity.status.ne(EntityStatus.DELETED))
                .fetch();
    }
}
