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
    void throwAppExceptionIfPasswordOverTwenty() {
        assertThatCode(() -> new Password("abc4567890123456789012"))
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.INVALID_PASSWORD_LENGTH);
    }

    @Test
    @DisplayName("비밀번호가 8자를 넘지 않으면 AppException이 발생한다.")
    void throwAppExceptionIfPasswordUnderEight() {
        assertThatCode(() -> new Password("abc1234"))
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.INVALID_PASSWORD_LENGTH);
    }

    @Test
    @DisplayName("비밀번호가 8자 이상 20자 이하면 정상 등록된다.")
    void savedIfPasswordIsOverEightAndUnderTwenty() {
        // given
        String passwordStr1 = "abc12345";
        String passwordStr2 = "abc12345678901234567";

        // when
        Password password1 = new Password(passwordStr1);
        Password password2 = new Password(passwordStr2);

        // then
        assertThat(password1.password()).isEqualTo(passwordStr1);
        assertThat(password2.password()).isEqualTo(passwordStr2);
    }

    @Test
    @DisplayName("비밀번호에 숫자만 들어가면 AppException이 발생한다.")
    void throwAppExceptionIfPasswordContainsNumberOnly() {
        assertThatCode(() -> new Password("12345678"))
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.INVALID_PASSWORD_FORMAT);
    }

    @Test
    @DisplayName("비밀번호에 영문만 들어가면 AppException이 발생한다.")
    void throwAppExceptionIfPasswordContainsEnglishOnly() {
        assertThatCode(() -> new Password("abcdefgh"))
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.INVALID_PASSWORD_FORMAT);
    }

    @Test
    @DisplayName("비밀번호에 숫자/영문을 제외한 문자가 들어가면 AppException이 발생한다.")
    void throwAppExceptionIfPasswordContainsSpecialCharacter() {
        assertThatCode(() -> new Password("가나다abcd12"))
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.INVALID_PASSWORD_FORMAT);
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