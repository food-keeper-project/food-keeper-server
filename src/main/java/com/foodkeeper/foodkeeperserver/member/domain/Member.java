package com.foodkeeper.foodkeeperserver.member.domain;

import com.foodkeeper.foodkeeperserver.member.domain.enums.Gender;
import com.foodkeeper.foodkeeperserver.member.domain.enums.SignUpType;
import lombok.Builder;

@Builder
public record Member(
        String id,
        String email,
        String nickname,
        Gender gender,
        SignUpType signUpType,
        String signUpIp,
        Boolean isDeleted
) {
}