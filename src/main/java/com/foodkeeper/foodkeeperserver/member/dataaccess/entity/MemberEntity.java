package com.foodkeeper.foodkeeperserver.member.dataaccess.entity;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.BaseEntity;
import com.foodkeeper.foodkeeperserver.member.domain.Member;
import com.foodkeeper.foodkeeperserver.member.domain.enums.Gender;
import com.foodkeeper.foodkeeperserver.member.domain.enums.SignUpType;
import jakarta.persistence.*;

@Entity
public class MemberEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String memberKey;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 20)
    private String nickname;

    @Column(nullable = false)
    private String imageUrl;

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
        memberEntity.memberKey = member.memberKey();
        memberEntity.email = member.email();
        memberEntity.nickname = member.nickname();
        memberEntity.imageUrl = member.imageUrl();
        memberEntity.gender = member.gender();
        memberEntity.signUpType = member.signUpType();
        memberEntity.signUpIp = member.signUpIp();
        return memberEntity;
    }

    public Member toDomain() {
        return Member.builder()
                .id(id)
                .memberKey(memberKey)
                .email(email)
                .nickname(nickname)
                .imageUrl(imageUrl)
                .gender(gender)
                .signUpType(signUpType)
                .signUpIp(signUpIp)
                .isDeleted(isDeleted())
                .build();
    }
}
