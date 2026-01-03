package com.foodkeeper.foodkeeperserver.member.domain;

import com.foodkeeper.foodkeeperserver.auth.domain.MemberRoles;
import com.foodkeeper.foodkeeperserver.member.domain.enums.SignUpType;
import lombok.Builder;
import org.jspecify.annotations.NullMarked;

@NullMarked
@Builder
public record NewMember(Email email,
                        Nickname nickname,
                        ProfileImageUrl imageUrl,
                        SignUpType signUpType,
                        IpAddress ipAddress,
                        MemberRoles memberRoles) {

    public String getEmail() {
        return email.email();
    }

    public String getNickname() {
        return nickname.nickname();
    }

    public String getImageUrl() {
        return imageUrl.imageUrl();
    }

    public String getIpAddress() {
        return ipAddress.ipAddress();
    }
}
