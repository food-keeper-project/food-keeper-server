package com.foodkeeper.foodkeeperserver.auth.business;

import com.foodkeeper.foodkeeperserver.auth.domain.*;
import com.foodkeeper.foodkeeperserver.auth.domain.enums.MemberRole;
import com.foodkeeper.foodkeeperserver.auth.implement.*;
import com.foodkeeper.foodkeeperserver.member.domain.NewLocalMember;
import com.foodkeeper.foodkeeperserver.member.domain.enums.SignUpType;
import com.foodkeeper.foodkeeperserver.member.implement.MemberFinder;
import com.foodkeeper.foodkeeperserver.member.implement.MemberRegistrar;
import com.foodkeeper.foodkeeperserver.notification.implement.FcmManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final OAuthAuthenticator oauthAuthenticator;
    private final LocalAuthAuthenticator localAuthAuthenticator;
    private final MemberFinder memberFinder;
    private final MemberRegistrar memberRegistrar;
    private final JwtGenerator jwtGenerator;
    private final RefreshTokenManager refreshTokenManager;
    private final ApplicationEventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Jwt signInByOAuth(SignInContext context) {
        OAuthUser oAuthUser = oauthAuthenticator.authenticate(context.accessToken());

        String memberKey = memberFinder.findMemberKeyByOAuthAccount(oAuthUser.account())
                .orElseGet(() -> memberRegistrar.register(oAuthUser.toNewOAuthMember(context.ipAddress())));

        Jwt jwt = jwtGenerator.generateJwt(memberKey);
        refreshTokenManager.updateRefreshToken(memberKey, jwt.refreshToken());

        eventPublisher.publishEvent(
                new SignInEvent(context.ipAddress(), context.fcmToken(), memberKey));

        return jwt;
    }

    public void signOut(String memberKey) {
        refreshTokenManager.remove(memberKey);
    }

    public void signUp(SignUpContext context) {
        memberRegistrar.register(context.toNewLocalMember(passwordEncoder.encode(context.password())));
    }

    public boolean isDuplicatedAccount(String account) {
        return localAuthAuthenticator.isDuplicatedAccount(account);
    }

    public boolean isDuplicatedEmail(String email) {
        return localAuthAuthenticator.isDuplicatedEmail(email);
    }
}
