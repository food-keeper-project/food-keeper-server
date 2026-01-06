package com.foodkeeper.foodkeeperserver.member.implement;

import com.foodkeeper.foodkeeperserver.member.dataaccess.entity.MemberEntity;
import com.foodkeeper.foodkeeperserver.member.dataaccess.repository.MemberRepository;
import com.foodkeeper.foodkeeperserver.member.domain.Member;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class MemberFinderTest {

    @Mock MemberRepository memberRepository;
    @InjectMocks MemberFinder memberFinder;

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
        Member foundMember = memberFinder.find(memberKey);

        // then
        assertThat(foundMember.memberKey()).isEqualTo("memberKey");
    }

    @Test
    @DisplayName("존재하지 않는 Member Key면 AppException이 발생한다.")
    void throwAppExceptionIfMemberKeyNotExist() {
        // given
        String memberKey = "notExistMemberKey";

        given(memberRepository.findByMemberKey(memberKey)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> memberFinder.find(memberKey))
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.NOT_FOUND_DATA);
    }
}