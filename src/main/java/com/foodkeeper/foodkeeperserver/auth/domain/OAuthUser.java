package com.foodkeeper.foodkeeperserver.auth.domain;

import com.foodkeeper.foodkeeperserver.auth.domain.enums.MemberRole;
import com.foodkeeper.foodkeeperserver.member.domain.NewMember;
import com.foodkeeper.foodkeeperserver.member.domain.NewOAuthMember;
import com.foodkeeper.foodkeeperserver.member.domain.enums.OAuthProvider;
import com.foodkeeper.foodkeeperserver.member.domain.enums.SignUpType;
import lombok.Builder;

import java.util.List;

@Builder
public record OAuthUser(String account,
                        OAuthProvider provider,
                        String nickname,
                        String email,
                        String profileImageUrl) {

    public NewOAuthMember toNewOAuthMember(String signUpIpAddress) {
        NewMember newMember = NewMember.builder()
                .email(email)
                .nickname(nickname)
                .imageUrl(profileImageUrl)
                .signUpType(SignUpType.OAUTH)
                .signUpIpAddress(signUpIpAddress)
                .memberRoles(new MemberRoles(List.of(MemberRole.ROLE_USER)))
                .build();
        return NewOAuthMember.builder()
                .member(newMember)
                .provider(provider)
                .oauthAccount(account)
                .build();
    }
}
