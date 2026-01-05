package com.foodkeeper.foodkeeperserver.auth.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.EmailVerificationEntity;
import com.foodkeeper.foodkeeperserver.auth.domain.enums.EmailVerificationStatus;
import com.foodkeeper.foodkeeperserver.support.repository.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class EmailVerificationRepositoryTest extends RepositoryTest {

    @Autowired EmailVerificationRepository emailVerificationRepository;

    @Test
    @DisplayName("EmailVerification들의 verificationStatus를 expired로 수정한다.")
    void updateVerificationsStatusToExpired() {
        // given
        String email = "test@mail.com";
        EmailVerificationEntity verification1 = em.persist(
                new EmailVerificationEntity(email, "123456", LocalDateTime.now().plusMinutes(5)));
        EmailVerificationEntity verification2 = em.persist(
                new EmailVerificationEntity(email, "123456", LocalDateTime.now().plusMinutes(5)));

        // when
        emailVerificationRepository.updateVerificationsStatusToExpired(email);

        // then
        Optional<EmailVerificationEntity> foundVerification1 = emailVerificationRepository.findById(verification1.getId());
        Optional<EmailVerificationEntity> foundVerification2 = emailVerificationRepository.findById(verification2.getId());
        assertThat(verification1.getVerificationStatus()).isEqualTo(EmailVerificationStatus.ACTIVE);
        assertThat(verification2.getVerificationStatus()).isEqualTo(EmailVerificationStatus.ACTIVE);
        assertThat(foundVerification1).isNotEmpty();
        assertThat(foundVerification1.get().getVerificationStatus()).isEqualTo(EmailVerificationStatus.EXPIRED);
        assertThat(foundVerification2).isNotEmpty();
        assertThat(foundVerification2.get().getVerificationStatus()).isEqualTo(EmailVerificationStatus.EXPIRED);
    }

    @Test
    @DisplayName("email로 해당 EmailVerification을 조회한다.")
    void findEmailVerificationByEmail() {
        String email = "test@mail.com";
        EmailVerificationEntity verification = em.persist(
                new EmailVerificationEntity(email, "123456", LocalDateTime.now().plusMinutes(5)));

        // when
        Optional<EmailVerificationEntity> foundVerification = emailVerificationRepository.findByEmail(email);

        // then
        assertThat(foundVerification).isNotEmpty();
        assertThat(foundVerification.get()).isEqualTo(verification);
    }

    @Test
    @DisplayName("EmailVerification의 verificationStatus를 expired로 수정한다.")
    void updateVerificationStatusToExpired() {
        // given
        String email = "test@mail.com";
        String code = "123456";
        EmailVerificationEntity verification = em.persist(
                new EmailVerificationEntity(email, code, LocalDateTime.now().plusMinutes(5)));

        // when
        emailVerificationRepository.updateStatusToExpired(email, code);

        // then
        Optional<EmailVerificationEntity> foundVerification = emailVerificationRepository.findById(verification.getId());
        assertThat(verification.getVerificationStatus()).isEqualTo(EmailVerificationStatus.ACTIVE);
        assertThat(foundVerification).isNotEmpty();
        assertThat(foundVerification.get().getVerificationStatus()).isEqualTo(EmailVerificationStatus.EXPIRED);
    }

    @Test
    @DisplayName("EmailVerification의 failedCount를 1 증가시킨다.")
    void incrementFailedCount() {
        // given
        String email = "test@mail.com";
        EmailVerificationEntity verification = em.persist(
                new EmailVerificationEntity(email, "123456", LocalDateTime.now().plusMinutes(5)));

        // when
        emailVerificationRepository.incrementFailedCount(email);

        // then
        Optional<EmailVerificationEntity> foundVerification = emailVerificationRepository.findById(verification.getId());
        assertThat(verification.getFailedCount()).isEqualTo(0);
        assertThat(foundVerification).isNotEmpty();
        assertThat(foundVerification.get().getFailedCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("EmailVerification의 verificationStatus를 verified로 수정한다.")
    void updateVerificationStatusToVerified() {
        // given
        String email = "test@mail.com";
        String code = "123456";
        EmailVerificationEntity verification = em.persist(
                new EmailVerificationEntity(email, code, LocalDateTime.now().plusMinutes(5)));

        // when
        emailVerificationRepository.updateStatusToVerified(email, code);

        // then
        Optional<EmailVerificationEntity> foundVerification = emailVerificationRepository.findById(verification.getId());
        assertThat(verification.getVerificationStatus()).isEqualTo(EmailVerificationStatus.ACTIVE);
        assertThat(foundVerification).isNotEmpty();
        assertThat(foundVerification.get().getVerificationStatus()).isEqualTo(EmailVerificationStatus.VERIFIED);
    }
}