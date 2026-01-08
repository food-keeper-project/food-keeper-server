package com.foodkeeper.foodkeeperserver.auth.domain;

import com.foodkeeper.foodkeeperserver.auth.domain.enums.OAuthProvider;
import com.foodkeeper.foodkeeperserver.member.domain.NewMember;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import lombok.Builder;

@Builder
public record NewOAuthMember(NewMember member,
                             OAuthProvider provider,
                             String oauthAccount) {

    public NewOAuthMember {
        if (provider == null || oauthAccount == null) {
            throw new AppException(ErrorType.DEFAULT_ERROR);
        }
    }
}
