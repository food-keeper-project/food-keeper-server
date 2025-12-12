package com.foodkeeper.foodkeeperserver.member.fixture;

import com.foodkeeper.foodkeeperserver.member.dataaccess.entity.MemberEntity;
import com.foodkeeper.foodkeeperserver.member.domain.Member;
import com.foodkeeper.foodkeeperserver.member.domain.enums.Gender;
import com.foodkeeper.foodkeeperserver.member.domain.enums.SignUpType;

public enum MemberEntityFixture {
    DEFAULT(1L, "memberKey", "member@mail.com", "nickname",
            "https://image.com", Gender.M, SignUpType.OAUTH, "123.123.123.123");
    private final Long id;
    private final String memberKey;
    private final String email;
    private final String nickname;
    private final String imageUrl;
    private final Gender gender;
    private final SignUpType signUpType;
    private final String signUpIp;

    MemberEntityFixture(Long id,
                        String memberKey,
                        String email,
                        String nickname,
                        String imageUrl,
                        Gender gender,
                        SignUpType signUpType,
                        String signUpIp) {
        this.id = id;
        this.memberKey = memberKey;
        this.email = email;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.gender = gender;
        this.signUpType = signUpType;
        this.signUpIp = signUpIp;
    }

    public MemberEntity get() {
        return MemberEntity.from(Member.builder()
                .id(id)
                .memberKey(memberKey)
                .email(email)
                .nickname(nickname)
                .imageUrl(imageUrl)
                .gender(gender)
                .signUpType(signUpType)
                .signUpIp(signUpIp)
                .build());
    }
}
