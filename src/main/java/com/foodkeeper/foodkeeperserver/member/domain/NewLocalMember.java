package com.foodkeeper.foodkeeperserver.member.domain;

import com.foodkeeper.foodkeeperserver.auth.domain.Account;
import com.foodkeeper.foodkeeperserver.auth.domain.EncodedPassword;
import lombok.Builder;

@Builder
public record NewLocalMember(NewMember member,
                             Account account,
                             EncodedPassword password) {

    public String getAccount() {
        return account.account();
    }

    public String getPassword() {
        return password.password();
    }
}
