package com.foodkeeper.foodkeeperserver.member.business;

import com.foodkeeper.foodkeeperserver.member.dataaccess.entity.MemberEntity;
import com.foodkeeper.foodkeeperserver.member.dataaccess.repository.MemberRepository;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.OauthRepository;
import com.foodkeeper.foodkeeperserver.member.domain.Member;
import com.foodkeeper.foodkeeperserver.member.implement.MemberFinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock MemberRepository memberRepository;
    @Mock OauthRepository oauthRepository;
    MemberService memberService;

    @BeforeEach
    void setUp() {
        MemberFinder memberFinder = new MemberFinder(memberRepository, oauthRepository);
        memberService = new MemberService(memberFinder);
    }

    @Test
    @DisplayName("멤버 Key를 통해 멤버를 조회한다.")
    void findMemberByKey() {
        // given
        String memberKey = "memberKey";
        MemberEntity memberEntity = mock(MemberEntity.class);
        Member member = Member.builder().memberKey(memberKey).build();
        given(memberRepository.findByMemberKey(memberKey)).willReturn(Optional.of(memberEntity));
        given(memberEntity.toDomain()).willReturn(member);

        // when
        Member foundMember = memberService.findMember(memberKey);

        // then
        assertThat(foundMember.memberKey()).isEqualTo("memberKey");
    }


}