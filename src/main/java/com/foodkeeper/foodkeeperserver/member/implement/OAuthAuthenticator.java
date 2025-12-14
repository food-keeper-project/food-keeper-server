package com.foodkeeper.foodkeeperserver.member.implement;

import com.foodkeeper.foodkeeperserver.member.domain.OAuthMember;

public interface OAuthAuthenticator {
    OAuthMember authenticate(String accessToken);
}
