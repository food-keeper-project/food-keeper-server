package com.foodkeeper.foodkeeperserver.member.business;

import com.foodkeeper.foodkeeperserver.member.domain.Member;
import com.foodkeeper.foodkeeperserver.member.implement.MemberFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberFinder memberFinder;

    public Member findMember(String memberId) {
        return memberFinder.find(memberId);
    }
}
