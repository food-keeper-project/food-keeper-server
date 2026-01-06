package com.foodkeeper.foodkeeperserver.common.enums;

import lombok.Getter;

@Getter
public enum IpHeader {
    X_FORWARDED_FOR("X-Forwarded-For"),
    X_REAL_IP("X-Real-IP"),
    PROXY_CLIENT_IP("Proxy-Client-IP"),
    WL_PROXY_CLIENT_IP("WL-Proxy-Client-IP"),
    HTTP_CLIENT_IP("HTTP_CLIENT_IP"),
    HTTP_X_FORWARDED_FOR("HTTP_X_FORWARDED_FOR"),
    ;

    private final String value;

    IpHeader(String value) {
        this.value = value;
    }
}
