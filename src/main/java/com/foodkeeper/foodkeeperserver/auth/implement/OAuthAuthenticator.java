package com.foodkeeper.foodkeeperserver.auth.implement;

import com.foodkeeper.foodkeeperserver.auth.domain.OAuthUser;

public interface OAuthAuthenticator {
    OAuthUser authenticate(String accessToken);
}
