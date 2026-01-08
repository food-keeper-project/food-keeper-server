package com.foodkeeper.foodkeeperserver.member.domain;

import com.foodkeeper.foodkeeperserver.auth.domain.LocalAccount;
import com.foodkeeper.foodkeeperserver.auth.domain.EncodedPassword;
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
