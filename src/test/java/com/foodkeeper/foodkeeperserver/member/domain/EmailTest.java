package com.foodkeeper.foodkeeperserver.member.domain;

import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class EmailTest {

    @Test
    @DisplayName("이메일이 null이면 AppException이 발생한다.")
    void throwAppExceptionIfEmailIsNull() {
        assertThatCode(() -> new Email(null))
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.INVALID_EMAIL);
    }

    @Test
    @DisplayName("이메일이 빈 값이면 AppException이 발생한다.")
    void throwAppExceptionIfEmailIsBlank() {
        assertThatCode(() -> new Email(" "))
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.INVALID_EMAIL);
    }

    @Test
    @DisplayName("이메일 형식이 맞지 않으면 AppException이 발생한다.")
    void throwAppExceptionIfIsInvalidEmail() {
        assertThatCode(() -> new Email("test#mail.com"))
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.INVALID_EMAIL);
    }

    @Test
    @DisplayName("이메일 형식이 맞으면 정상등록 된다.")
    void savedIfEmailIsValid() {
        // given
        String emailStr = "test@mail.com";

        // when
        Email email = new Email(emailStr);

        // then
        assertThat(email.email()).isEqualTo(emailStr);
    }
}