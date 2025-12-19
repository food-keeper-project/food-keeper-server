package com.foodkeeper.foodkeeperserver.auth.business;

import com.foodkeeper.foodkeeperserver.auth.domain.Jwt;
import com.foodkeeper.foodkeeperserver.auth.domain.MemberRoles;
import com.foodkeeper.foodkeeperserver.auth.domain.OAuthUser;
import com.foodkeeper.foodkeeperserver.auth.domain.SignInContext;
import com.foodkeeper.foodkeeperserver.auth.domain.enums.MemberRole;
import com.foodkeeper.foodkeeperserver.auth.implement.JwtGenerator;
import com.foodkeeper.foodkeeperserver.auth.implement.OAuthAuthenticator;
import com.foodkeeper.foodkeeperserver.auth.implement.RefreshTokenManager;
import com.foodkeeper.foodkeeperserver.auth.implement.SignInLogAppender;
import com.foodkeeper.foodkeeperserver.member.domain.enums.SignUpType;
import com.foodkeeper.foodkeeperserver.member.implement.MemberFinder;
import com.foodkeeper.foodkeeperserver.member.implement.MemberRegistrar;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final OAuthAuthenticator oauthAuthenticator;
    private final MemberFinder memberFinder;
    private final MemberRegistrar memberRegistrar;
    private final SignInLogAppender signInLogAppender;
    private final JwtGenerator jwtGenerator;
    private final RefreshTokenManager refreshTokenManager;

    public Jwt signInByOAuth(SignInContext context) {
        OAuthUser oAuthUser = oauthAuthenticator.authenticate(context.accessToken());

        String memberKey = memberFinder.findMemberKeyByOAuthAccount(oAuthUser.account())
                .orElseGet(() ->
                        memberRegistrar.register(oAuthUser.toNewOAuthMember(
                                SignUpType.OAUTH, context.ipAddress(), new MemberRoles(List.of(MemberRole.ROLE_USER))))
                );

        signInLogAppender.append(context.ipAddress(), memberKey);

        Jwt jwt = jwtGenerator.generateJwt(memberKey);
        refreshTokenManager.updateRefreshToken(memberKey, jwt.refreshToken());
        return jwt;
    }

    public void signOut(String memberKey) {
        refreshTokenManager.remove(memberKey);
    }
}
