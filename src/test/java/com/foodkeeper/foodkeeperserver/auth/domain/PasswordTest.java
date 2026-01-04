package com.foodkeeper.foodkeeperserver.auth.domain;

import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class PasswordTest {

    @Test
    @DisplayName("비밀번호가 20자를 넘으면 AppException이 발생한다.")
    void throwAppExceptionIfPasswordOver20() {
        assertThatCode(() -> new Password("1234567890123456789012"))
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.INVALID_PASSWORD_LENGTH);
    }

    @Test
    @DisplayName("비밀번호가 20자 이하면 정상 등록된다.")
    void savedIfPasswordIsValid() {
        // given
        String passwordStr = "12345678901234567890";

        // when
        Password password = new Password(passwordStr);

        // then
        assertThat(password.password()).isEqualTo(passwordStr);
    }

    @Test
    @DisplayName("비밀번호에 null이 들어가면 AppException이 발생한다.")
    void throwAppExceptionIfPasswordIsNull() {
        assertThatCode(() -> new Password(null))
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.PASSWORD_IS_NULL);
    }
}