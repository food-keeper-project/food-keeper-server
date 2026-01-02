package com.foodkeeper.foodkeeperserver.auth.domain;

import com.foodkeeper.foodkeeperserver.auth.domain.enums.MemberRole;
import com.foodkeeper.foodkeeperserver.member.domain.NewLocalMember;
import com.foodkeeper.foodkeeperserver.member.domain.NewMember;
import com.foodkeeper.foodkeeperserver.member.domain.enums.Gender;
import com.foodkeeper.foodkeeperserver.member.domain.enums.SignUpType;
import lombok.Builder;

import java.util.List;

@Builder
public record SignUpContext(String account,
                            String password,
                            String email,
                            String nickname,
                            Gender gender,
                            List<String> preferFoods,
                            String ipAddress) {

    public NewLocalMember toNewLocalMember(String encodedPassword) {
        NewMember newMember = NewMember.builder()
                .email(email)
                .nickname(nickname)
                .imageUrl("")
                .signUpType(SignUpType.LOCAL)
                .signUpIpAddress(ipAddress)
                .memberRoles(new MemberRoles(List.of(MemberRole.ROLE_USER)))
                .build();
        return NewLocalMember.builder()
                .member(newMember)
                .account(account)
                .password(encodedPassword)
                .build();
    }
}
