package com.foodkeeper.foodkeeperserver.auth.domain;

import com.foodkeeper.foodkeeperserver.member.domain.NewMember;
import com.foodkeeper.foodkeeperserver.member.domain.NewOAuthMember;
import com.foodkeeper.foodkeeperserver.member.domain.enums.OAuthProvider;
import com.foodkeeper.foodkeeperserver.member.domain.enums.SignUpType;
import lombok.Builder;

@Builder
public record OAuthUser(String account,
                        OAuthProvider provider,
                        String nickname,
                        String email,
                        String profileImageUrl) {

    public NewOAuthMember toNewOAuthMember(SignUpType signUpType, String signUpIpAddress, MemberRoles memberRoles) {
        NewMember newMember = NewMember.builder()
                .email(email)
                .nickname(nickname)
                .imageUrl(profileImageUrl)
                .signUpType(signUpType)
                .signUpIpAddress(signUpIpAddress)
                .memberRoles(memberRoles)
                .build();
        return NewOAuthMember.builder()
                .member(newMember)
                .provider(provider)
                .oauthAccount(account)
                .build();
    }
}
