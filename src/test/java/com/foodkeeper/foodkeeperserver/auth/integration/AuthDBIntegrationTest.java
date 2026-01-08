package com.foodkeeper.foodkeeperserver.auth.integration;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.EmailVerificationEntity;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.EmailVerificationRepository;
import com.foodkeeper.foodkeeperserver.auth.domain.EmailVerification;
import com.foodkeeper.foodkeeperserver.auth.domain.enums.EmailVerificationStatus;
import com.foodkeeper.foodkeeperserver.auth.implement.EmailVerificator;
import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.enums.EntityStatus;
import com.foodkeeper.foodkeeperserver.mail.implement.AppMailSender;
import com.foodkeeper.foodkeeperserver.member.domain.Email;
import com.foodkeeper.foodkeeperserver.support.integration.SpringTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthDBIntegrationTest extends SpringTest {

    @MockitoBean
    AppMailSender appMailSender;
    @Autowired
    EmailVerificationRepository emailVerificationRepository;
    @Autowired
    EmailVerificator emailVerificator;

    @Test
    @DisplayName("조회된 EmailVerification의 만료 시간이 지났을 경우 EXPIRED 및 삭제 처리한다.")
    void setExpiredAndDeleteEmailVerificationIfExpired() {
        String email = "test@mail.com";
        String code = "123456";
        EmailVerification emailVerification = emailVerificationRepository
                .save(new EmailVerificationEntity(email, code, LocalDateTime.now().plusMinutes(5)))
                .toDomain();

        // when
        emailVerification.expire();
        emailVerificator.updateVerification(emailVerification);


        Optional<EmailVerificationEntity> foundVerification =
                emailVerificationRepository.findById(emailVerification.getId());
        assertThat(foundVerification).isNotEmpty();
        assertThat(foundVerification.get().getVerificationStatus()).isEqualTo(EmailVerificationStatus.EXPIRED);
        assertThat(foundVerification.get().getStatus()).isEqualTo(EntityStatus.DELETED);
    }
}
