package com.foodkeeper.foodkeeperserver.auth.dataaccess.entity;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "local_auth")
public class LocalAuthEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "local_auth_id")
    private Long id;

    @Column(nullable = false)
    private String account;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDateTime passwordUpdatedAt;

    @Column(nullable = false)
    private String memberKey;

    public LocalAuthEntity(String account, String password, String memberKey) {
        this.account = account;
        this.password = password;
        this.passwordUpdatedAt = LocalDateTime.now();
        this.memberKey = memberKey;
    }
}
