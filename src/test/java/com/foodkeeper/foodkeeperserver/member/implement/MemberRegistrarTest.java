package com.foodkeeper.foodkeeperserver.member.implement;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.MemberRoleRepository;
import com.foodkeeper.foodkeeperserver.member.dataaccess.entity.MemberEntity;
import com.foodkeeper.foodkeeperserver.member.dataaccess.repository.MemberRepository;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.OauthRepository;
import com.foodkeeper.foodkeeperserver.member.domain.NewMember;
import com.foodkeeper.foodkeeperserver.auth.domain.OAuthMember;
import com.foodkeeper.foodkeeperserver.member.domain.enums.OAuthProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class MemberRegistrarTest {

    @Mock MemberRepository memberRepository;
    @Mock OauthRepository oauthRepository;
    @Mock MemberRoleRepository memberRoleRepository;
    MemberRegistrar memberRegistrar;

    @BeforeEach
    void setUp() {
        memberRegistrar = new MemberRegistrar(memberRepository, oauthRepository, memberRoleRepository);
    }

    @Test
    @DisplayName("새 멤버를 등록한다.")
    void registerMember() {
        // given
        OAuthMember oauthMember = OAuthMember.builder()
                .account("account")
                .provider(OAuthProvider.KAKAO)
                .build();
        NewMember newMember = mock(NewMember.class);
        MemberEntity memberEntity = mock(MemberEntity.class);
        given(memberEntity.getMemberKey()).willReturn("memberKey");
        given(memberRepository.save(any(MemberEntity.class))).willReturn(memberEntity);

        // when
        String registeredMemberKey = memberRegistrar.register(newMember, oauthMember);

        // then
        assertThat(registeredMemberKey).isEqualTo("memberKey");
    }
}