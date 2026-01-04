package com.foodkeeper.foodkeeperserver.member.domain;

import com.foodkeeper.foodkeeperserver.member.domain.enums.Gender;
import com.foodkeeper.foodkeeperserver.member.domain.enums.SignUpType;
import lombok.Builder;
import org.jspecify.annotations.NullMarked;

@NullMarked
@Builder
public record Member(
        Long id,
        String memberKey,
        String email,
        String nickname,
        String imageUrl,
        Gender gender,
        SignUpType signUpType,
        String signUpIp,
        Boolean isDeleted
) {}