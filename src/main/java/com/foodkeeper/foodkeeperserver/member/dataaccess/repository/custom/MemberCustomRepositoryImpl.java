package com.foodkeeper.foodkeeperserver.member.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.enums.EntityStatus;
import com.foodkeeper.foodkeeperserver.member.dataaccess.entity.MemberEntity;
import com.foodkeeper.foodkeeperserver.support.repository.QuerydslRepositorySupport;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.Optional;

import static com.foodkeeper.foodkeeperserver.member.dataaccess.entity.QMemberEntity.memberEntity;

public class MemberCustomRepositoryImpl extends QuerydslRepositorySupport implements MemberCustomRepository {

    protected MemberCustomRepositoryImpl() {
        super(MemberEntity.class);
    }

    @Override
    public Optional<MemberEntity> findByMemberKey(String memberKey) {
        return Optional.ofNullable(
                selectFrom(memberEntity)
                        .where(eqMember(memberKey), isActive())
                        .fetchOne()
        );
    }

    @Override
    public Optional<String> findRefreshToken(String memberKey) {
        return Optional.ofNullable(
                select(memberEntity.refreshToken)
                        .from(memberEntity)
                        .where(eqMember(memberKey), isActive())
                        .fetchOne()
        );
    }

    @Override
    public void updateRefreshToken(String memberKey, String refreshToken) {
        update(memberEntity)
                .set(memberEntity.refreshToken, refreshToken)
                .where(eqMember(memberKey), isActive())
                .execute();

        getEntityManager().clear();
    }

    @Override
    public void deleteRefreshTokenByMemberKey(String memberKey) {
        update(memberEntity)
                .setNull(memberEntity.refreshToken)
                .where(eqMember(memberKey), isActive())
                .execute();

        getEntityManager().clear();
    }

    @Override
    public boolean existsByEmail(String email) {
        Integer fetchOne = selectOne()
                .from(memberEntity)
                .where(memberEntity.email.eq(email), isActive())
                .fetchFirst();

        return fetchOne != null;
    }

    private static BooleanExpression eqMember(String memberKey) {
        return memberEntity.memberKey.eq(memberKey);
    }

    private static BooleanExpression isActive() {
        return memberEntity.status.eq(EntityStatus.ACTIVE);
    }
}
