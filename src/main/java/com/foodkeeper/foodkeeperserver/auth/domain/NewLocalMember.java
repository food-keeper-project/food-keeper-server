package com.foodkeeper.foodkeeperserver.auth.domain;

import com.foodkeeper.foodkeeperserver.member.domain.NewMember;
import lombok.Builder;

@Builder
public record NewLocalMember(NewMember member,
                             LocalAccount account,
                             EncodedPassword password) {

    public String getAccount() {
        return account.account();
    }

    public String getPassword() {
        return password.password();
    }
}
