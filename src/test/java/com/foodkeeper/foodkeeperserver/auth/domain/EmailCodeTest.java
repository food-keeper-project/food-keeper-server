package com.foodkeeper.foodkeeperserver.auth.domain;

import com.foodkeeper.foodkeeperserver.member.domain.Email;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class EmailCodeTest {

    @Test
    @DisplayName("6자리 숫자를 정상적으로 등록한다.")
    void saveEmailCodeWith6Digits() {
        EmailCode emailCode = new EmailCode(new Email("test@mail.com"), "123456");

        assertThat(emailCode.code()).isEqualTo("123456");
    }

    @Test
    @DisplayName("숫자가 6자리가 아니면 AppException이 발생한다.")
    void throwAppExceptionIfNot6Digits() {
        assertThatCode(() -> new EmailCode(new Email("test@mail.com"), "12346"))
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.INVALID_EMAIL_CODE);
    }

    @Test
    @DisplayName("숫자가 0으로 시작하면 AppException이 발생한다.")
    void throwAppExceptionIfFirstDigitIsZero() {
        assertThatCode(() -> new EmailCode(new Email("test@mail.com"), "012346"))
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.INVALID_EMAIL_CODE);
    }
}