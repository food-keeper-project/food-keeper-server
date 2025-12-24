package com.foodkeeper.foodkeeperserver.auth.dataaccess.entity;

import com.foodkeeper.foodkeeperserver.auth.domain.enums.MemberRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "member_role")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_role_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role;

    @Column(nullable = false)
    private String memberKey;

    public MemberRoleEntity(MemberRole role, String memberKey) {
        this.role = role;
        this.memberKey = memberKey;
    }
}
