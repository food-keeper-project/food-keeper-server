package com.foodkeeper.foodkeeperserver.auth.controller.v1.request;

import com.foodkeeper.foodkeeperserver.auth.domain.SignUpContext;
import com.foodkeeper.foodkeeperserver.member.domain.enums.Gender;
import com.foodkeeper.foodkeeperserver.support.validation.MemberEmail;
import jakarta.validation.constraints.Size;

public record LocalSignUpRequest(@Size(max = 20) String account,
                                 @Size(max = 20) String password,
                                 @MemberEmail String email,
                                 @Size(max = 20) String nickname,
                                 Gender gender) {

    public SignUpContext toContext(String ipAddress) {
        return SignUpContext.builder()
                .account(account)
                .password(password)
                .email(email)
                .nickname(nickname)
                .gender(gender)
                .ipAddress(ipAddress)
                .build();
    }
}
