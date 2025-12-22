package com.foodkeeper.foodkeeperserver.member.implement;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.OauthEntity;
import com.foodkeeper.foodkeeperserver.member.dataaccess.repository.MemberRepository;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.OauthRepository;
import com.foodkeeper.foodkeeperserver.member.domain.Member;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberFinder {
    private final MemberRepository memberRepository;
    private final OauthRepository oauthRepository;

    public Member find(String memberKey) {
        return memberRepository.findByMemberKey(memberKey).orElseThrow(() -> new AppException(ErrorType.NOT_FOUND_DATA))
                .toDomain();
    }

    public Optional<String> findMemberKeyByOAuthAccount(String oAuthAccount) {
        return oauthRepository.findByAccount(oAuthAccount).map(OauthEntity::getMemberKey);
    }
}
