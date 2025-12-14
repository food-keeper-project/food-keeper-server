package com.foodkeeper.foodkeeperserver.member.dataaccess.entity;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.BaseEntity;
import com.foodkeeper.foodkeeperserver.member.domain.Member;
import com.foodkeeper.foodkeeperserver.member.domain.NewMember;
import com.foodkeeper.foodkeeperserver.member.domain.enums.Gender;
import com.foodkeeper.foodkeeperserver.member.domain.enums.SignUpType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "member")
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
    private String signUpIpAddress;

    public static MemberEntity from(NewMember member) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.memberKey = UUID.randomUUID().toString();
        memberEntity.email = member.email();
        memberEntity.nickname = member.nickname();
        memberEntity.imageUrl = member.imageUrl();
        memberEntity.gender = Gender.N;
        memberEntity.signUpType = member.signUpType();
        memberEntity.signUpIpAddress = member.signUpIpAddress();
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
                .signUpIp(signUpIpAddress)
                .isDeleted(isDeleted())
                .build();
    }
}
