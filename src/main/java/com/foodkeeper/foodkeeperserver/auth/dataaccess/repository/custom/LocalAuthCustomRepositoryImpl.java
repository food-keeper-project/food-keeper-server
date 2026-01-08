package com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.LocalAuthEntity;
import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.enums.EntityStatus;
import com.foodkeeper.foodkeeperserver.support.repository.QuerydslRepositorySupport;

import java.util.Optional;

import static com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.QLocalAuthEntity.localAuthEntity;
import static com.foodkeeper.foodkeeperserver.member.dataaccess.entity.QMemberEntity.memberEntity;

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

    @Override
    public boolean existsByEmail(String email) {
        Integer fetchOne = selectOne()
                .from(localAuthEntity)
                .innerJoin(memberEntity)
                .on(localAuthEntity.memberKey.eq(memberEntity.memberKey))
                .where(memberEntity.email.eq(email),
                        localAuthEntity.status.ne(EntityStatus.DELETED),
                        memberEntity.status.ne(EntityStatus.DELETED)
                )
                .fetchOne();

        return fetchOne != null;
    }

    @Override
    public boolean existsByEmailAndAccount(String email, String account) {
        Integer fetchOne = selectOne()
                .from(localAuthEntity)
                .innerJoin(memberEntity)
                .on(localAuthEntity.memberKey.eq(memberEntity.memberKey))
                .where(memberEntity.email.eq(email),
                        localAuthEntity.account.eq(account),
                        localAuthEntity.status.ne(EntityStatus.DELETED),
                        memberEntity.status.ne(EntityStatus.DELETED)
                )
                .fetchOne();

        return fetchOne != null;
    }

    @Override
    public Optional<LocalAuthEntity> findByAccountAndPassword(String account, String encodedPassword) {
        return Optional.ofNullable(
                selectFrom(localAuthEntity)
                        .where(localAuthEntity.account.eq(account), localAuthEntity.password.eq(encodedPassword))
                        .where(localAuthEntity.status.ne(EntityStatus.DELETED))
                        .fetchOne()
        );
    }

    @Override
    public Optional<LocalAuthEntity> findByEmail(String email) {
        return Optional.ofNullable(
                selectFrom(localAuthEntity)
                        .innerJoin(memberEntity)
                        .on(localAuthEntity.memberKey.eq(memberEntity.memberKey))
                        .where(memberEntity.email.eq(email),
                                localAuthEntity.status.ne(EntityStatus.DELETED),
                                memberEntity.status.ne(EntityStatus.DELETED)
                        )
                        .fetchOne()
        );
    }

    @Override
    public Optional<LocalAuthEntity> findByEmailAndAccount(String email, String account) {
        return Optional.ofNullable(
                selectFrom(localAuthEntity)
                        .innerJoin(memberEntity)
                        .on(localAuthEntity.memberKey.eq(memberEntity.memberKey))
                        .where(memberEntity.email.eq(email),
                                localAuthEntity.account.eq(account),
                                localAuthEntity.status.ne(EntityStatus.DELETED),
                                memberEntity.status.ne(EntityStatus.DELETED)
                        )
                        .fetchOne()
        );
    }
}
