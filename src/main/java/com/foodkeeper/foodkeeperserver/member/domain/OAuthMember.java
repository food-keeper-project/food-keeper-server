package com.foodkeeper.foodkeeperserver.member.domain;

import com.foodkeeper.foodkeeperserver.member.domain.enums.OAuthProvider;
import lombok.Builder;

@Builder
public record OAuthMember(String account,
                          OAuthProvider provider,
                          String nickname,
                          String email,
                          String profileImageUrl) {
}
