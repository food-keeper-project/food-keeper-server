package com.foodkeeper.foodkeeperserver.member.dataaccess.entity;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.BaseEntity;
import com.foodkeeper.foodkeeperserver.member.domain.Member;
import com.foodkeeper.foodkeeperserver.member.domain.enums.Gender;
import com.foodkeeper.foodkeeperserver.member.domain.enums.SignUpType;
import jakarta.persistence.*;

@Entity
public class MemberEntity extends BaseEntity {

    @Id
    @Column(name = "member_id")
    private String id;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 20)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "CHAR", length = 2)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR", length = 20)
    private SignUpType signUpType;

    @Column(nullable = false, length = 20)
    private String signUpIp;

    public static MemberEntity from(Member member) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.email = member.email();
        memberEntity.nickname = member.nickname();
        memberEntity.gender = member.gender();
        memberEntity.signUpType = member.signUpType();
        memberEntity.signUpIp = member.signUpIp();
        return memberEntity;
    }

    public Member toDomain() {
        return Member.builder()
                .id(id)
                .email(email)
                .nickname(nickname)
                .gender(gender)
                .signUpType(signUpType)
                .signUpIp(signUpIp)
                .isDeleted(isDeleted())
                .build();
    }
}
