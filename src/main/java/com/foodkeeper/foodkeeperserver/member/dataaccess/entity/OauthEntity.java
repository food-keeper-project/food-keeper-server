package com.foodkeeper.foodkeeperserver.member.dataaccess.entity;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.BaseEntity;
import com.foodkeeper.foodkeeperserver.member.domain.enums.OAuthProvider;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "oauth")
public class OauthEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oauth_id")
    private Long id;

    @Column(nullable = false, length = 20)
    private OAuthProvider provider;

    @Column(nullable = false, length = 20)
    private String account;

    @Column(nullable = false)
    private String memberKey;

    public OauthEntity(OAuthProvider provider, String account, String memberKey) {
        this.provider = provider;
        this.account = account;
        this.memberKey = memberKey;
    }
}
