package com.foodkeeper.foodkeeperserver.auth.domain;

public record LocalAuth(LocalAccount account, EncodedPassword password, String memberKey) {
    public String getAccount() {
        return account.account();
    }
}
