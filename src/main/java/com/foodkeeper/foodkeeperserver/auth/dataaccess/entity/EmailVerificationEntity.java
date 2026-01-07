package com.foodkeeper.foodkeeperserver.auth.dataaccess.entity;

import com.foodkeeper.foodkeeperserver.auth.domain.EmailCode;
import com.foodkeeper.foodkeeperserver.auth.domain.EmailVerification;
import com.foodkeeper.foodkeeperserver.auth.domain.enums.EmailVerificationStatus;
import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "email_verification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailVerificationEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_verification_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private Integer failedCount;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EmailVerificationStatus verificationStatus;

    public EmailVerificationEntity(String email, String code, LocalDateTime expiredAt) {
        this.email = email;
        this.code = code;
        this.failedCount = 0;
        this.expiredAt = expiredAt;
        this.verificationStatus = EmailVerificationStatus.ACTIVE;
    }

    public EmailVerification toDomain() {
        return EmailVerification.builder()
                .emailCode(EmailCode.of(email, code))
                .failedCount(failedCount)
                .expiredAt(expiredAt)
                .status(verificationStatus)
                .build();
    }
}
