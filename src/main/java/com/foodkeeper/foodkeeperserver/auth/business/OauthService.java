package com.foodkeeper.foodkeeperserver.auth.business;

import com.foodkeeper.foodkeeperserver.auth.domain.Jwt;
import com.foodkeeper.foodkeeperserver.auth.domain.OAuthUser;
import com.foodkeeper.foodkeeperserver.auth.domain.OauthSignInContext;
import com.foodkeeper.foodkeeperserver.auth.domain.SignInEvent;
import com.foodkeeper.foodkeeperserver.auth.implement.JwtGenerator;
import com.foodkeeper.foodkeeperserver.auth.implement.OAuthAuthenticator;
import com.foodkeeper.foodkeeperserver.auth.implement.OauthLockManager;
import com.foodkeeper.foodkeeperserver.auth.implement.OauthRegistrar;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthService {
    private final OAuthAuthenticator oauthAuthenticator;
    private final OauthRegistrar oauthRegistrar;
    private final JwtGenerator jwtGenerator;
    private final ApplicationEventPublisher eventPublisher;
    private final OauthLockManager lockManager;

    public Jwt signInByOAuth(OauthSignInContext context) {
        OAuthUser oAuthUser = oauthAuthenticator.authenticate(context.accessToken());

        String memberKey = registerIfNewAndGetMemberKey(context, oAuthUser);

        Jwt jwt = jwtGenerator.generateJwt(memberKey);

        eventPublisher.publishEvent(
                new SignInEvent(context.getIpAddress(), jwt.refreshToken(), context.fcmToken(), memberKey));

        return jwt;
    }

    private String registerIfNewAndGetMemberKey(OauthSignInContext context, OAuthUser oAuthUser) {
        String lockKey = context.oAuthProvider().name() + ":" + oAuthUser.email();
        try {
            lockManager.acquire(lockKey, 3);
            return oauthRegistrar.registerIfNewAndGetMemberKey(oAuthUser.toNewOAuthMember(context.ipAddress()));
        } finally {
            lockManager.release(lockKey);
        }
    }
}
