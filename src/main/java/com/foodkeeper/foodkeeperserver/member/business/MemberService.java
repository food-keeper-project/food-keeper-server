package com.foodkeeper.foodkeeperserver.member.business;

import com.foodkeeper.foodkeeperserver.member.domain.Member;
import com.foodkeeper.foodkeeperserver.member.implement.MemberFinder;
import com.foodkeeper.foodkeeperserver.member.implement.MemberWithdrawalProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberFinder memberFinder;
    private final MemberWithdrawalProcessor memberWithdrawalProcessor;

    public Member findMember(String memberKey) {
        return memberFinder.find(memberKey);
    }

    public void withdrawMember(String memberKey) {
        memberWithdrawalProcessor.withdraw(memberKey);
    }
}
