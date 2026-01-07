package com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.EmailVerificationEntity;
import com.foodkeeper.foodkeeperserver.auth.domain.enums.EmailVerificationStatus;
import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.enums.EntityStatus;
import com.foodkeeper.foodkeeperserver.support.repository.QuerydslRepositorySupport;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.Optional;

import static com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.QEmailVerificationEntity.emailVerificationEntity;

public class EmailVerificationCustomRepositoryImpl extends QuerydslRepositorySupport
        implements EmailVerificationCustomRepository {

    protected EmailVerificationCustomRepositoryImpl() {
        super(EmailVerificationEntity.class);
    }

    @Override
    public void updateVerificationsStatusToExpired(String email) {
        update(emailVerificationEntity)
                .set(emailVerificationEntity.verificationStatus, EmailVerificationStatus.EXPIRED)
                .where(isActive())
                .where(emailVerificationEntity.email.eq(email))
                .execute();

        getEntityManager().clear();
    }

    @Override
    public Optional<EmailVerificationEntity> findByEmail(String email) {
        return Optional.ofNullable(
                selectFrom(emailVerificationEntity)
                        .where(emailVerificationEntity.email.eq(email))
                        .where(isActive())
                        .fetchOne()
        );
    }

    @Override
    public void updateStatusToExpired(String email, String code) {
        update(emailVerificationEntity)
                .set(emailVerificationEntity.verificationStatus, EmailVerificationStatus.EXPIRED)
                .where(isActive())
                .where(emailVerificationEntity.email.eq(email), emailVerificationEntity.code.eq(code))
                .execute();

        getEntityManager().clear();
    }

    @Override
    public void incrementFailedCount(String email) {
        update(emailVerificationEntity)
                .set(emailVerificationEntity.failedCount, emailVerificationEntity.failedCount.add(1))
                .where(isActive())
                .where(emailVerificationEntity.email.eq(email))
                .execute();

        getEntityManager().clear();
    }

    @Override
    public void updateStatusToVerified(String email, String code) {
        update(emailVerificationEntity)
                .set(emailVerificationEntity.verificationStatus, EmailVerificationStatus.VERIFIED)
                .where(isActive())
                .where(emailVerificationEntity.email.eq(email), emailVerificationEntity.code.eq(code))
                .execute();

        getEntityManager().clear();
    }

    private static BooleanExpression isActive() {
        return emailVerificationEntity.status.ne(EntityStatus.DELETED).and(
                emailVerificationEntity.verificationStatus.eq(EmailVerificationStatus.ACTIVE));
    }
}
