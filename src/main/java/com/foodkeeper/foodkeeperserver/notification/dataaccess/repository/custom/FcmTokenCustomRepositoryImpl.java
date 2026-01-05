package com.foodkeeper.foodkeeperserver.notification.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.enums.EntityStatus;
import com.foodkeeper.foodkeeperserver.notification.dataaccess.entity.FcmTokenEntity;
import com.foodkeeper.foodkeeperserver.support.repository.QuerydslRepositorySupport;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.foodkeeper.foodkeeperserver.notification.dataaccess.entity.QFcmTokenEntity.fcmTokenEntity;

public class FcmTokenCustomRepositoryImpl extends QuerydslRepositorySupport implements FcmTokenCustomRepository {

    protected FcmTokenCustomRepositoryImpl() {
        super(FcmTokenEntity.class);
    }

    @Override
    public void deleteFcmTokens(String memberKey) {
        update(fcmTokenEntity)
                .set(fcmTokenEntity.status, EntityStatus.DELETED)
                .where(fcmTokenEntity.memberKey.eq(memberKey), isNotDeleted())
                .execute();
    }

    @Override
    public List<FcmTokenEntity> findAllByMemberKeyIn(Set<String> memberKeys) {
        return selectFrom(fcmTokenEntity)
                .where(fcmTokenEntity.memberKey.in(memberKeys), isNotDeleted())
                .fetch();
    }

    @Override
    public Optional<FcmTokenEntity> findByToken(String token) {
        return Optional.ofNullable(
                selectFrom(fcmTokenEntity)
                        .where(fcmTokenEntity.token.eq(token), isNotDeleted())
                        .fetchOne()
        );
    }

    @Override
    public void deleteByToken(String token) {
        update(fcmTokenEntity)
                .set(fcmTokenEntity.status, EntityStatus.DELETED)
                .where(fcmTokenEntity.token.eq(token), isNotDeleted())
                .execute();
    }

    private static BooleanExpression isNotDeleted() {
        return fcmTokenEntity.status.ne(EntityStatus.DELETED);
    }
}
