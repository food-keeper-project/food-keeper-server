package com.foodkeeper.foodkeeperserver.member.domain;

import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;

import java.util.regex.Pattern;

public record IpAddress(String ipAddress) {

    private static final Pattern IPV4_PATTERN = Pattern.compile("^(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)" +
            "(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}$");

    public IpAddress {
        validateIpAddress(ipAddress);
    }

    private static void validateIpAddress(String ipAddress) {
        if (ipAddress == null || !IPV4_PATTERN.matcher(ipAddress).matches()) {
            throw new AppException(ErrorType.INVALID_ACCESS_PATH);
        }
    }
}
