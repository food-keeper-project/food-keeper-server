package com.foodkeeper.foodkeeperserver.auth.implement;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.OauthEntity;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.OauthRepository;
import com.foodkeeper.foodkeeperserver.auth.domain.NewOAuthMember;
import com.foodkeeper.foodkeeperserver.member.implement.MemberRegistrar;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OAuthRegistrar {
    private final OauthRepository oauthRepository;
    private final MemberRegistrar memberRegistrar;

    @Transactional
    public String registerIfNewAndGetMemberKey(NewOAuthMember newOAuthMember) {
        return oauthRepository.findByEmail(newOAuthMember.member().getEmail(), newOAuthMember.provider())
                .map(OauthEntity::getMemberKey)
                .orElseGet(() -> register(newOAuthMember));
    }

    private String register(NewOAuthMember newOAuthMember) {
        String memberKey = memberRegistrar.register(newOAuthMember.member());
        oauthRepository.save(new OauthEntity(newOAuthMember.provider(), newOAuthMember.oauthAccount(), memberKey));
        return memberKey;
    }
}
