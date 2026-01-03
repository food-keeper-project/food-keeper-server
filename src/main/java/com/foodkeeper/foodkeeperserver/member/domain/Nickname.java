package com.foodkeeper.foodkeeperserver.member.domain;

import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import com.google.common.base.Strings;

public record Nickname(String nickname) {
    private static final int NICKNAME_MAX_LENGTH = 20;

    public Nickname {
        nickname = Strings.nullToEmpty(nickname);
        validateNickname(nickname);
    }

    private static void validateNickname(String nickname) {
        if (nickname.length() > NICKNAME_MAX_LENGTH) {
            throw new AppException(ErrorType.INVALID_NICKNAME_LENGTH);
        }
    }
}
