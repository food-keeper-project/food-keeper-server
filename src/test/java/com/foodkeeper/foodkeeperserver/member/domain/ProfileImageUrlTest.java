package com.foodkeeper.foodkeeperserver.member.domain;

import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class ProfileImageUrlTest {

    @Test
    @DisplayName("프로필 이미지 URL 형식이 맞지 않으면 AppException이 발생한다.")
    void throwAppExceptionIfIsInvalidImageUrl() {
        assertThatCode(() -> new ProfileImageUrl("https:/test/image.jpg"))
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.INVALID_IMAGE_URL);
    }

    @Test
    @DisplayName("프로필 이미지에 null이 들어가면 빈 값으로 반환된다.")
    void returnEmptyStringIfProfileImageUrlIsNull() {
        // given
        String nullImageUrl = null;

        // when
        ProfileImageUrl profileImageUrl = new ProfileImageUrl(nullImageUrl);

        // then
        assertThat(profileImageUrl.imageUrl()).isEqualTo("");
    }
}