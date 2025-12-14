package com.foodkeeper.foodkeeperserver.member.domain;

import com.foodkeeper.foodkeeperserver.auth.domain.MemberRole;
import com.foodkeeper.foodkeeperserver.member.domain.enums.SignUpType;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import lombok.Builder;

@Builder
public record NewMember(String email,
                        String nickname,
                        String imageUrl,
                        SignUpType signUpType,
                        String signUpIpAddress,
                        MemberRole memberRole) {

    private static final int NICKNAME_MAX_LENGTH = 20;

    public NewMember {
        if (nickname.length() > NICKNAME_MAX_LENGTH) {
            throw new AppException(ErrorType.DEFAULT_ERROR);
        }
    }
}
