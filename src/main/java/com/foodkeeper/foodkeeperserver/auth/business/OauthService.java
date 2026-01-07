package com.foodkeeper.foodkeeperserver.auth.business;

import com.foodkeeper.foodkeeperserver.auth.domain.Jwt;
import com.foodkeeper.foodkeeperserver.auth.domain.OAuthUser;
import com.foodkeeper.foodkeeperserver.auth.domain.SignInContext;
import com.foodkeeper.foodkeeperserver.auth.domain.SignInEvent;
import com.foodkeeper.foodkeeperserver.auth.implement.JwtGenerator;
import com.foodkeeper.foodkeeperserver.auth.implement.OAuthAuthenticator;
import com.foodkeeper.foodkeeperserver.auth.implement.OauthFinder;
import com.foodkeeper.foodkeeperserver.auth.implement.OauthLockManager;
import com.foodkeeper.foodkeeperserver.member.implement.MemberRegistrar;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthService {
    private final OAuthAuthenticator oauthAuthenticator;
    private final OauthFinder oauthFinder;
    private final MemberRegistrar memberRegistrar;
    private final JwtGenerator jwtGenerator;
    private final ApplicationEventPublisher eventPublisher;
    private final OauthLockManager lockManager;

    public Jwt signInByOAuth(SignInContext context) {
        OAuthUser oAuthUser = oauthAuthenticator.authenticate(context.accessToken());

        String memberKey = registerIfNotExists(context, oAuthUser);

        Jwt jwt = jwtGenerator.generateJwt(memberKey);

        eventPublisher.publishEvent(
                new SignInEvent(context.getIpAddress(), jwt.refreshToken(), context.fcmToken(), memberKey));

        return jwt;
    }

    /** @return MemberKey(멤버 키) */
    private String registerIfNotExists(SignInContext context, OAuthUser oAuthUser) {
        String lockKey = context.oAuthProvider().name() + oAuthUser.email();
        try {
            lockManager.acquire(lockKey, 1);
            return oauthFinder.findMemberKey(oAuthUser.email(), oAuthUser.provider())
                    .orElseGet(() -> memberRegistrar.register(oAuthUser.toNewOAuthMember(context.ipAddress())));
        } finally {
            lockManager.release(lockKey);
        }
    }
}
