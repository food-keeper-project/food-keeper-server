package com.foodkeeper.foodkeeperserver.member.domain;

import com.foodkeeper.foodkeeperserver.auth.domain.MemberRoles;
import com.foodkeeper.foodkeeperserver.auth.domain.enums.MemberRole;
import com.foodkeeper.foodkeeperserver.member.domain.enums.SignUpType;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NewMemberTest {

    @Test
    @DisplayName("닉네임이 20자를 넘으면 AppException이 발생한다.")
    void throwAppExceptionIfNicknameOver20() {
        assertThatThrownBy(() -> NewMember.builder()
                .nickname("1234567890123456789012")
                .email("btac3310@gmail.com")
                .imageUrl("https://test.com/image.jpg")
                .signUpType(SignUpType.OAUTH)
                .signUpIpAddress("127.0.0.1")
                .memberRoles(new MemberRoles(List.of(MemberRole.ROLE_USER)))
                .build())
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.INVALID_NICKNAME_LENGTH);
    }

    @Test
    @DisplayName("이메일이 null이면 AppException이 발생한다.")
    void throwAppExceptionIfEmailIsNull() {
        assertThatThrownBy(() -> NewMember.builder()
                .nickname("1234567890")
                .email(null)
                .imageUrl("https://test.com/image.jpg")
                .signUpType(SignUpType.OAUTH)
                .signUpIpAddress("127.0.0.1")
                .memberRoles(new MemberRoles(List.of(MemberRole.ROLE_USER)))
                .build())
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.INVALID_EMAIL);
    }

    @Test
    @DisplayName("이메일 형식이 맞지 않으면 AppException이 발생한다.")
    void throwAppExceptionIfIsInvalidEmail() {
        assertThatThrownBy(() -> NewMember.builder()
                .nickname("1234567890")
                .email("test#mail.com")
                .imageUrl("https://test.com/image.jpg")
                .signUpType(SignUpType.OAUTH)
                .signUpIpAddress("127.0.0.1")
                .memberRoles(new MemberRoles(List.of(MemberRole.ROLE_USER)))
                .build())
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.INVALID_EMAIL);
    }

    @Test
    @DisplayName("이미지 URL 형식이 맞지 않으면 AppException이 발생한다.")
    void throwAppExceptionIfIsInvalidImageUrl() {
        assertThatThrownBy(() -> NewMember.builder()
                .nickname("1234567890")
                .email("test@mail.com")
                .imageUrl("https:/test/image.jpg")
                .signUpType(SignUpType.OAUTH)
                .signUpIpAddress("127.0.0.1")
                .memberRoles(new MemberRoles(List.of(MemberRole.ROLE_USER)))
                .build())
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.INVALID_IMAGE_URL);
    }
}