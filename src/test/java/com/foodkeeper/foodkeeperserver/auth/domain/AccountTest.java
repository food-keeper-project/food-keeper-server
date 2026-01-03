package com.foodkeeper.foodkeeperserver.auth.domain;

import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class AccountTest {

    @Test
    @DisplayName("계정이 20자를 넘으면 AppException이 발생한다.")
    void throwAppExceptionIfAccountOver20() {
        assertThatCode(() -> new Account("1234567890123456789012"))
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.INVALID_ACCOUNT_LENGTH);
    }

    @Test
    @DisplayName("계정이 20자 이하면 정상 등록된다.")
    void savedIfAccountIsValid() {
        // given
        String accountStr = "12345678901234567890";

        // when
        Account account = new Account(accountStr);

        // then
        assertThat(account.account()).isEqualTo(accountStr);
    }

    @Test
    @DisplayName("계정에 null이 들어가면 AppException이 발생한다.")
    void throwAppExceptionIfAccountIsNull() {
        assertThatCode(() -> new Account(null))
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.ACCOUNT_IS_NULL);
    }
}