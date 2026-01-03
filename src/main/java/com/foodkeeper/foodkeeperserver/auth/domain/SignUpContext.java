package com.foodkeeper.foodkeeperserver.auth.domain;

import com.foodkeeper.foodkeeperserver.auth.domain.enums.MemberRole;
import com.foodkeeper.foodkeeperserver.member.domain.*;
import com.foodkeeper.foodkeeperserver.member.domain.enums.Gender;
import com.foodkeeper.foodkeeperserver.member.domain.enums.SignUpType;
import lombok.Builder;

import java.util.List;

@Builder
public record SignUpContext(Account account,
                            Password password,
                            Email email,
                            Nickname nickname,
                            Gender gender,
                            List<String> preferFoods,
                            IpAddress ipAddress) {
    public SignUpContext {
        if (preferFoods == null) {
            preferFoods = List.of();
        }
    }

    public NewLocalMember toNewLocalMember(String encodedPassword) {
        NewMember newMember = NewMember.builder()
                .email(email)
                .nickname(nickname)
                .imageUrl(ProfileImageUrl.empty())
                .signUpType(SignUpType.LOCAL)
                .ipAddress(ipAddress)
                .memberRoles(new MemberRoles(List.of(MemberRole.ROLE_USER)))
                .build();
        return NewLocalMember.builder()
                .member(newMember)
                .account(account)
                .password(new EncodedPassword(encodedPassword))
                .build();
    }

    public String getPassword() {
        return password.password();
    }
}
