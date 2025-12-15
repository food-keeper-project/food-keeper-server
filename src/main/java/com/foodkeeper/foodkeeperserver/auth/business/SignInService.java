package com.foodkeeper.foodkeeperserver.auth.business;

import com.foodkeeper.foodkeeperserver.auth.domain.*;
import com.foodkeeper.foodkeeperserver.auth.implement.JwtGenerator;
import com.foodkeeper.foodkeeperserver.auth.implement.OAuthAuthenticator;
import com.foodkeeper.foodkeeperserver.auth.implement.SignInLogAppender;
import com.foodkeeper.foodkeeperserver.member.domain.NewMember;
import com.foodkeeper.foodkeeperserver.member.domain.enums.SignUpType;
import com.foodkeeper.foodkeeperserver.member.implement.MemberFinder;
import com.foodkeeper.foodkeeperserver.member.implement.MemberRegistrar;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SignInService {
    private final OAuthAuthenticator oauthAuthenticator;
    private final JwtGenerator jwtGenerator;
    private final SignInLogAppender signInLogAppender;
    private final MemberFinder memberFinder;
    private final MemberRegistrar memberRegistrar;

    public Jwt signInByOAuth(MemberRegister register) {
        OAuthMember oAuthMember = oauthAuthenticator.authenticate(register.accessToken());

        String memberKey;
        if (memberFinder.existsByOauthAccount(oAuthMember.account())) {
            memberKey = memberFinder.findMemberKeyByOAuthAccount(oAuthMember.account());
        } else {
            NewMember newMember = NewMember.builder()
                    .email(oAuthMember.email())
                    .nickname(oAuthMember.nickname())
                    .imageUrl(oAuthMember.profileImageUrl())
                    .signUpType(SignUpType.OAUTH)
                    .signUpIpAddress(register.ipAddress())
                    .memberRoles(new MemberRoles(List.of(MemberRole.ROLE_USER)))
                    .build();
            memberKey = memberRegistrar.register(newMember, oAuthMember);
        }

        signInLogAppender.append(register.ipAddress(), memberKey);

        return jwtGenerator.generateJwt(memberKey);
    }
}
