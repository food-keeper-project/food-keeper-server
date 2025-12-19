package com.foodkeeper.foodkeeperserver.auth.implement;

import com.foodkeeper.foodkeeperserver.member.dataaccess.repository.MemberRepository;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RefreshTokenManager {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public String find(String memberKey) {
        return memberRepository.findRefreshToken(memberKey).orElseThrow(() -> new AppException(ErrorType.FAILED_AUTH));
    }

    @Transactional
    public void updateRefreshToken(String memberKey, String refreshToken) {
        memberRepository.updateRefreshToken(memberKey, refreshToken);
    }

    @Transactional
    public void remove(String memberKey) {
        memberRepository.deleteRefreshTokenByMemberKey(memberKey);
    }
}
