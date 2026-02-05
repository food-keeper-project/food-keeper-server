package com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.OauthEntity;
import com.foodkeeper.foodkeeperserver.auth.domain.enums.OAuthProvider;
import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.enums.EntityStatus;
import com.foodkeeper.foodkeeperserver.support.repository.QuerydslRepositorySupport;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.List;
import java.util.Optional;

import static com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.QOauthEntity.oauthEntity;
import static com.foodkeeper.foodkeeperserver.member.dataaccess.entity.QMemberEntity.memberEntity;

public class OauthCustomRepositoryImpl extends QuerydslRepositorySupport implements OauthCustomRepository {

    protected OauthCustomRepositoryImpl() {
        super(OauthEntity.class);
    }

    @Override
    public Optional<OauthEntity> findByEmail(String email, OAuthProvider provider) {
        return Optional.ofNullable(
                selectFrom(oauthEntity)
                        .innerJoin(memberEntity)
                        .on(oauthEntity.memberKey.eq(memberEntity.memberKey))
                        .where(memberEntity.email.eq(email),
                                oauthEntity.provider.eq(provider),
                                isActive(),
                                memberEntity.status.eq(EntityStatus.ACTIVE)
                        )
                        .fetchOne()
        );
    }

    @Override
    public List<OauthEntity> findAllByMemberKey(String memberKey) {
        return selectFrom(oauthEntity)
                .where(oauthEntity.memberKey.eq(memberKey), isActive())
                .fetch();
    }

    private static BooleanExpression isActive() {
        return oauthEntity.status.eq(EntityStatus.ACTIVE);
    }
}
