package com.foodkeeper.foodkeeperserver.auth.controller.v1.request;

import com.foodkeeper.foodkeeperserver.auth.domain.SignUpContext;
import com.foodkeeper.foodkeeperserver.member.domain.enums.Gender;

import java.util.List;

public record LocalSignUpRequest(String account,
                                 String password,
                                 String email,
                                 String nickname,
                                 Gender gender,
                                 List<String> preferFoods) {

    public SignUpContext toContext() {
        return SignUpContext.builder()
                .account(account)
                .password(password)
                .email(email)
                .nickname(nickname)
                .gender(gender)
                .preferFoods(preferFoods)
                .build();
    }
}
