package com.foodkeeper.foodkeeperserver.member.implement;

import com.foodkeeper.foodkeeperserver.member.dataaccess.entity.MemberEntity;
import com.foodkeeper.foodkeeperserver.member.dataaccess.entity.OauthEntity;
import com.foodkeeper.foodkeeperserver.member.dataaccess.repository.MemberRepository;
import com.foodkeeper.foodkeeperserver.member.dataaccess.repository.OauthRepository;
import com.foodkeeper.foodkeeperserver.member.domain.Member;
import com.foodkeeper.foodkeeperserver.member.domain.NewMember;
import com.foodkeeper.foodkeeperserver.member.domain.OAuthMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberRegistrar {
    private final MemberRepository memberRepository;
    private final OauthRepository oauthRepository;

    public String register(NewMember newMember, OAuthMember oauthMember) {
        MemberEntity memberEntity = memberRepository.save(MemberEntity.from(newMember));
        oauthRepository.save(new OauthEntity(oauthMember.provider(), oauthMember.account(), memberEntity.getMemberKey()));

        return memberEntity.getMemberKey();
    }
}
