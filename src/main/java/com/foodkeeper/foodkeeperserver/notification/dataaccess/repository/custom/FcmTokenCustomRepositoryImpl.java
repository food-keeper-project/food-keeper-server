package com.foodkeeper.foodkeeperserver.notification.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.enums.EntityStatus;
import com.foodkeeper.foodkeeperserver.notification.dataaccess.entity.FcmTokenEntity;
import com.foodkeeper.foodkeeperserver.support.repository.QuerydslRepositorySupport;

import static com.foodkeeper.foodkeeperserver.notification.dataaccess.entity.QFcmTokenEntity.fcmTokenEntity;

public class FcmTokenCustomRepositoryImpl extends QuerydslRepositorySupport implements FcmTokenCustomRepository {

    protected FcmTokenCustomRepositoryImpl() {
        super(FcmTokenEntity.class);
    }

    @Override
    public void deleteFcmTokens(String memberKey) {
        update(fcmTokenEntity)
                .set(fcmTokenEntity.status, EntityStatus.DELETED)
                .where(fcmTokenEntity.memberKey.eq(memberKey), fcmTokenEntity.status.ne(EntityStatus.DELETED))
                .execute();
    }
}
