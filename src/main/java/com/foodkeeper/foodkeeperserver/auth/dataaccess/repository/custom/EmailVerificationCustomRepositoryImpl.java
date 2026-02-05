package com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.EmailVerificationEntity;
import com.foodkeeper.foodkeeperserver.auth.domain.enums.EmailVerificationStatus;
import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.enums.EntityStatus;
import com.foodkeeper.foodkeeperserver.support.repository.QuerydslRepositorySupport;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.QEmailVerificationEntity.emailVerificationEntity;

public class EmailVerificationCustomRepositoryImpl extends QuerydslRepositorySupport
        implements EmailVerificationCustomRepository {

    protected EmailVerificationCustomRepositoryImpl() {
        super(EmailVerificationEntity.class);
    }

    @Override
    @Transactional
    public void updateVerificationsStatusToExpired(String email) {
        update(emailVerificationEntity)
                .set(emailVerificationEntity.verificationStatus, EmailVerificationStatus.EXPIRED)
                .set(emailVerificationEntity.status, EntityStatus.DELETED)
                .set(emailVerificationEntity.deletedAt, LocalDateTime.now())
                .where(emailVerificationEntity.email.eq(email))
                .where(isActive())
                .execute();

        getEntityManager().clear();
    }

    @Override
    public Optional<EmailVerificationEntity> findByEmail(String email) {
        return Optional.ofNullable(
                selectFrom(emailVerificationEntity)
                        .where(emailVerificationEntity.email.eq(email))
                        .where(isActive())
                        .fetchFirst()
        );
    }

    @Override
    @Transactional
    public void incrementFailedCount(String email) {
        update(emailVerificationEntity)
                .set(emailVerificationEntity.failedCount, emailVerificationEntity.failedCount.add(1))
                .where(emailVerificationEntity.email.eq(email))
                .where(isActive(), emailVerificationEntity.verificationStatus.eq(EmailVerificationStatus.ACTIVE))
                .execute();

        getEntityManager().clear();
    }

    private static BooleanExpression isActive() {
        return emailVerificationEntity.status.eq(EntityStatus.ACTIVE);
    }
}
