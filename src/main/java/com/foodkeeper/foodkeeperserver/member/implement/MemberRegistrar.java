package com.foodkeeper.foodkeeperserver.member.implement;

import com.foodkeeper.foodkeeperserver.member.dataaccess.entity.MemberEntity;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.OauthEntity;
import com.foodkeeper.foodkeeperserver.member.dataaccess.repository.MemberRepository;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.OauthRepository;
import com.foodkeeper.foodkeeperserver.member.domain.NewMember;
import com.foodkeeper.foodkeeperserver.auth.domain.OAuthMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MemberRegistrar {
    private final MemberRepository memberRepository;
    private final OauthRepository oauthRepository;

    @Transactional
    public String register(NewMember newMember, OAuthMember oauthMember) {
        MemberEntity memberEntity = memberRepository.save(MemberEntity.from(newMember));
        oauthRepository.save(new OauthEntity(oauthMember.provider(), oauthMember.account(), memberEntity.getMemberKey()));

        return memberEntity.getMemberKey();
    }
}
