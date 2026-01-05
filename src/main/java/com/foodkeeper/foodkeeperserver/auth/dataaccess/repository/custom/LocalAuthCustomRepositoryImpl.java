package com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.LocalAuthEntity;
import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.enums.EntityStatus;
import com.foodkeeper.foodkeeperserver.support.repository.QuerydslRepositorySupport;

import static com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.QLocalAuthEntity.localAuthEntity;

public class LocalAuthCustomRepositoryImpl extends QuerydslRepositorySupport implements LocalAuthCustomRepository {

    protected LocalAuthCustomRepositoryImpl() {
        super(LocalAuthEntity.class);
    }

    public boolean existsByAccount(String account) {
        Integer fetchOne = selectOne()
                .from(localAuthEntity)
                .where(localAuthEntity.account.eq(account), localAuthEntity.status.ne(EntityStatus.DELETED))
                .fetchFirst();

        return fetchOne != null;
    }
}
