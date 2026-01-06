package com.foodkeeper.foodkeeperserver.auth.business;

import com.foodkeeper.foodkeeperserver.auth.domain.Jwt;
import com.foodkeeper.foodkeeperserver.auth.domain.OAuthUser;
import com.foodkeeper.foodkeeperserver.auth.domain.SignInContext;
import com.foodkeeper.foodkeeperserver.auth.domain.SignInEvent;
import com.foodkeeper.foodkeeperserver.auth.implement.JwtGenerator;
import com.foodkeeper.foodkeeperserver.auth.implement.OAuthAuthenticator;
import com.foodkeeper.foodkeeperserver.auth.implement.OauthFinder;
import com.foodkeeper.foodkeeperserver.member.implement.MemberRegistrar;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OauthService {
    private final OAuthAuthenticator oauthAuthenticator;
    private final OauthFinder oauthFinder;
    private final MemberRegistrar memberRegistrar;
    private final JwtGenerator jwtGenerator;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Jwt signInByOAuth(SignInContext context) {
        OAuthUser oAuthUser = oauthAuthenticator.authenticate(context.accessToken());

        String memberKey = oauthFinder.findMemberKeyByOAuthAccount(oAuthUser.account())
                .orElseGet(() -> memberRegistrar.register(oAuthUser.toNewOAuthMember(context.ipAddress())));

        Jwt jwt = jwtGenerator.generateJwt(memberKey);

        eventPublisher.publishEvent(
                new SignInEvent(context.getIpAddress(), jwt.refreshToken(), context.fcmToken(), memberKey));

        return jwt;
    }
}
