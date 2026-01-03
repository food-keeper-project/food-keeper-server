package com.foodkeeper.foodkeeperserver.member.domain;

import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class NicknameTest {

    @Test
    @DisplayName("닉네임이 20자를 넘으면 AppException이 발생한다.")
    void throwAppExceptionIfNicknameOver20() {
        assertThatCode(() -> new Nickname("1234567890123456789012"))
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.INVALID_NICKNAME_LENGTH);
    }

    @Test
    @DisplayName("닉네임이 20자 이하면 정상 등록된다.")
    void savedIfNicknameIsValid() {
        // given
        String nicknameStr = "12345678901234567890";

        // when
        Nickname nickname = new Nickname(nicknameStr);

        // then
        assertThat(nickname.nickname()).isEqualTo(nicknameStr);
    }

    @Test
    @DisplayName("닉네임에 null이 들어가면 빈 값으로 반환된다.")
    void returnEmptyStringIfNicknameIsNull() {
        // given
        String nullNickname = null;

        // when
        Nickname nickname = new Nickname(nullNickname);

        // then
        assertThat(nickname.nickname()).isEqualTo("");
    }
}