package com.foodkeeper.foodkeeperserver.member.implement;

import com.foodkeeper.foodkeeperserver.member.dataaccess.entity.MemberEntity;
import com.foodkeeper.foodkeeperserver.member.dataaccess.repository.MemberRepository;
import com.foodkeeper.foodkeeperserver.member.domain.Member;
import com.foodkeeper.foodkeeperserver.member.fixture.MemberEntityFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberFinderTest {

    @Mock MemberRepository memberRepository;
    MemberFinder memberFinder;

    @BeforeEach
    void setUp() {
        memberFinder = new MemberFinder(memberRepository);
    }

    @Test
    @DisplayName("멤버 Key를 통해 멤버를 조회한다.")
    void findMemberByKey() {
        // given
        String memberKey = "memberKey";
        MemberEntity memberEntity = MemberEntityFixture.DEFAULT.get();
        given(memberRepository.findByMemberKey(memberKey)).willReturn(Optional.of(memberEntity));

        // when
        Member member = memberFinder.find(memberKey);

        // then
        assertThat(member.memberKey()).isEqualTo("memberKey");
    }
}