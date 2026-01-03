package com.foodkeeper.foodkeeperserver.auth.controller.v1.request;

import com.foodkeeper.foodkeeperserver.auth.domain.Account;
import com.foodkeeper.foodkeeperserver.auth.domain.Password;
import com.foodkeeper.foodkeeperserver.auth.domain.SignUpContext;
import com.foodkeeper.foodkeeperserver.member.domain.Email;
import com.foodkeeper.foodkeeperserver.member.domain.Nickname;
import com.foodkeeper.foodkeeperserver.member.domain.enums.Gender;
import com.foodkeeper.foodkeeperserver.support.validation.MemberEmail;
import jakarta.validation.constraints.Size;

import java.util.List;

public record LocalSignUpRequest(@Size(max = 20) String account,
                                 @Size(max = 20) String password,
                                 @MemberEmail String email,
                                 @Size(max = 20) String nickname,
                                 Gender gender,
                                 List<String> preferFoods) {

    public SignUpContext toContext() {
        return SignUpContext.builder()
                .account(new Account(account))
                .password(new Password(password))
                .email(new Email(email))
                .nickname(new Nickname(nickname))
                .gender(gender)
                .preferFoods(preferFoods)
                .build();
    }
}
