package com.foodkeeper.foodkeeperserver.member.implement;

import com.foodkeeper.foodkeeperserver.member.dataaccess.entity.MemberEntity;
import com.foodkeeper.foodkeeperserver.member.dataaccess.entity.OauthEntity;
import com.foodkeeper.foodkeeperserver.member.dataaccess.repository.MemberRepository;
import com.foodkeeper.foodkeeperserver.member.dataaccess.repository.OauthRepository;
import com.foodkeeper.foodkeeperserver.member.domain.Member;
import com.foodkeeper.foodkeeperserver.member.domain.enums.OAuthProvider;
import com.foodkeeper.foodkeeperserver.member.fixture.MemberEntityFixture;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class MemberFinderTest {

    @Mock MemberRepository memberRepository;
    @Mock OauthRepository oauthRepository;
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

    @Test
    @DisplayName("OAuth Account와 일치하는 멤버가 있으면 true를 반환한다.")
    void returnTrueIfMemberExistsByOAuthAccount() {
        // given
        String oauthAccount = "oauthAccount";
        given(oauthRepository.existsByAccount(eq("oauthAccount"))).willReturn(true);

        // when
        boolean exists = memberFinder.existsByOauthAccount(oauthAccount);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("OAuth Account와 일치하는 멤버가 없으면 false를 반환한다.")
    void returnFalseIfMemberNotExistsByOAuthAccount() {
        // given
        String oauthAccount = "oauthAccount";
        given(oauthRepository.existsByAccount(any())).willReturn(false);

        // when
        boolean exists = memberFinder.existsByOauthAccount(oauthAccount);

        // then
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("OAuth Account와 일치하는 멤버의 멤버키를 조회한다.")
    void findMemberKeyByOAuthAccount() {
        // given
        String oauthAccount = "oauthAccount";
        OauthEntity oauthEntity = new OauthEntity(OAuthProvider.KAKAO, oauthAccount, "memberKey");
        given(oauthRepository.findByAccount(eq(oauthAccount))).willReturn(Optional.of(oauthEntity));

        // when
        String memberKey = memberFinder.findMemberKeyByOAuthAccount(oauthAccount);

        // then
        assertThat(memberKey).isEqualTo("memberKey");
    }

    @Test
    @DisplayName("존재하지 않는 OAuthAccount이면 AppException이 발생한다.")
    void throwAppExceptionIfOAuthAccountNotExist() {
        // given
        String oauthAccount = "notExistOAuthAccount";

        given(oauthRepository.findByAccount(oauthAccount)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> memberFinder.findMemberKeyByOAuthAccount(oauthAccount))
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.NOT_FOUND_DATA);
    }
}