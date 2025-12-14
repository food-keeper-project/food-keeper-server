package com.foodkeeper.foodkeeperserver.member.business;

import com.foodkeeper.foodkeeperserver.member.domain.Jwt;
import com.foodkeeper.foodkeeperserver.member.domain.MemberRegister;
import com.foodkeeper.foodkeeperserver.member.domain.NewMember;
import com.foodkeeper.foodkeeperserver.member.domain.OAuthMember;
import com.foodkeeper.foodkeeperserver.member.domain.enums.SignUpType;
import com.foodkeeper.foodkeeperserver.member.implement.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignInService {
    private final MemberFinder memberFinder;
    private final MemberRegistrar memberRegistrar;
    private final OAuthAuthenticator oauthAuthenticator;
    private final JwtGenerator jwtGenerator;
    private final SignInLogAppender signInLogAppender;

    @Transactional
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
                    .build();
            memberKey = memberRegistrar.register(newMember, oAuthMember);
        }

        signInLogAppender.append(register.ipAddress(), memberKey);

        return jwtGenerator.generateJwt(memberKey);
    }
}
