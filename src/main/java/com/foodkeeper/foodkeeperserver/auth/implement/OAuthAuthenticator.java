package com.foodkeeper.foodkeeperserver.auth.implement;

import com.foodkeeper.foodkeeperserver.auth.domain.OAuthMember;

public interface OAuthAuthenticator {
    OAuthMember authenticate(String accessToken);
}
