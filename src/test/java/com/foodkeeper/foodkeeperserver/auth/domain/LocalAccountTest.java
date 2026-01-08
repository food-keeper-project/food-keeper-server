package com.foodkeeper.foodkeeperserver.auth.domain;

import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class LocalAccountTest {

    @Test
    @DisplayName("계정이 12자를 넘으면 AppException이 발생한다.")
    void throwAppExceptionIfAccountOverTwelve() {
        assertThatCode(() -> new LocalAccount("1234567890123"))
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.INVALID_ACCOUNT_LENGTH);
    }

    @Test
    @DisplayName("계정이 6자를 넘지 않으면 AppException이 발생한다.")
    void throwAppExceptionIfAccountUnderSix() {
        assertThatCode(() -> new LocalAccount("12345"))
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.INVALID_ACCOUNT_LENGTH);
    }

    @Test
    @DisplayName("계정이 6자 이상 12자 이하면 정상 등록된다.")
    void savedIfAccountUnderTwelve() {
        // given
        String accountStr1 = "123456";
        String accountStr2 = "123456789012";

        // when
        LocalAccount localAccount1 = new LocalAccount(accountStr1);
        LocalAccount localAccount2 = new LocalAccount(accountStr2);

        // then
        assertThat(localAccount1.account()).isEqualTo(accountStr1);
        assertThat(localAccount2.account()).isEqualTo(accountStr2);
    }

    @Test
    @DisplayName("계정에 숫자/영문을 제외한 문자가 들어가면 AppException이 발생한다.")
    void throwAppExceptionIfAccountContainsSpecialCharacter() {
        assertThatCode(() -> new LocalAccount("가나다abcd12"))
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.INVALID_ACCOUNT_FORMAT);
    }

    @Test
    @DisplayName("계정에 null이 들어가면 AppException이 발생한다.")
    void throwAppExceptionIfAccountIsNull() {
        assertThatCode(() -> new LocalAccount(null))
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.ACCOUNT_IS_NULL);
    }
}