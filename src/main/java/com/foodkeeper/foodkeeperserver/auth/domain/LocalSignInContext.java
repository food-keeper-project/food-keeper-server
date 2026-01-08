package com.foodkeeper.foodkeeperserver.auth.domain;

import com.foodkeeper.foodkeeperserver.member.domain.IpAddress;

public record LocalSignInContext(LocalAccount account,
                                 Password password,
                                 String fcmToken,
                                 IpAddress ipAddress) {

    public static LocalSignInContext of(String account, String password, String fcmToken, String ipAddress) {
        return new LocalSignInContext(
                new LocalAccount(account),
                new Password(password),
                fcmToken,
                new IpAddress(ipAddress)
        );
    }

    public String getIpAddress() {
        return ipAddress.ipAddress();
    }
}
