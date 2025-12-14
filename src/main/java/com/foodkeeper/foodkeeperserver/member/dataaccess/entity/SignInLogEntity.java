package com.foodkeeper.foodkeeperserver.member.dataaccess.entity;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "sign_in_log")
public class SignInLogEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sign_in_log_id")
    private Long id;

    @Column(nullable = false, length = 20)
    private String ipAddress;

    @Column(nullable = false)
    private String memberKey;

    public SignInLogEntity(String ipAddress, String memberKey) {
        this.ipAddress = ipAddress;
        this.memberKey = memberKey;
    }
}
