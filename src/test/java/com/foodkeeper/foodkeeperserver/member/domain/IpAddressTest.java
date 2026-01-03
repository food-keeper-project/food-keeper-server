package com.foodkeeper.foodkeeperserver.member.domain;

import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class IpAddressTest {

    @Test
    @DisplayName("IPv4 형식이 맞지 않으면 AppException이 발생한다.")
    void throwAppExceptionIfIsInvalidIPv4() {
        assertThatCode(() -> new IpAddress("123.11.1245.34"))
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.INVALID_ACCESS_PATH);
    }

    @Test
    @DisplayName("IpAddress 에 null이 들어가면 AppException이 발생한다.")
    void throwAppExceptionIfIpAddressIsNull() {
        assertThatCode(() -> new IpAddress(null))
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.INVALID_ACCESS_PATH);
    }

    @Test
    @DisplayName("IPv4 형식이 맞으면 정상등록 된다.")
    void savedIfIpAddressIsValid() {
        // given
        String ipStr = "123.123.123.123";

        // when
        IpAddress ipAddress = new IpAddress(ipStr);

        // then
        assertThat(ipAddress.ipAddress()).isEqualTo(ipStr);
    }
}