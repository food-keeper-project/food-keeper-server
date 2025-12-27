package com.foodkeeper.foodkeeperserver.notification.dataaccess.entity;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.BaseEntity;
import com.foodkeeper.foodkeeperserver.notification.domain.FcmToken;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "fcm_token")
public class FcmTokenEntity extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fcm_token_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private String memberKey;

    @Builder
    private FcmTokenEntity(String fcmToken, String memberKey) {
        this.token = fcmToken;
        this.memberKey = memberKey;
    }

    public FcmToken toDomain() {
        return new FcmToken(
                this.id,
                this.token,
                this.getCreatedAt(),
                this.getUpdatedAt(),
                this.memberKey
        );
    }

    public static FcmTokenEntity from(FcmToken fcmToken) {
        return FcmTokenEntity.builder()
                .fcmToken(fcmToken.fcmToken())
                .memberKey(fcmToken.memberKey())
                .build();
    }
    public void changeMemberKey(String memberKey) {
        this.memberKey = memberKey;
    }

}
