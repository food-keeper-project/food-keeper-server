package com.foodkeeper.foodkeeperserver.member.implement;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.MemberRoleRepository;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.OauthRepository;
import com.foodkeeper.foodkeeperserver.auth.domain.MemberRoles;
import com.foodkeeper.foodkeeperserver.auth.domain.enums.MemberRole;
import com.foodkeeper.foodkeeperserver.food.implement.FoodCategoryManager;
import com.foodkeeper.foodkeeperserver.member.dataaccess.entity.MemberEntity;
import com.foodkeeper.foodkeeperserver.member.dataaccess.repository.MemberRepository;
import com.foodkeeper.foodkeeperserver.member.domain.NewMember;
import com.foodkeeper.foodkeeperserver.member.domain.NewOAuthMember;
import com.foodkeeper.foodkeeperserver.member.domain.enums.OAuthProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class MemberRegistrarTest {

    @Mock MemberRepository memberRepository;
    @Mock OauthRepository oauthRepository;
    @Mock MemberRoleRepository memberRoleRepository;
    @Mock FoodCategoryManager foodCategoryManager;
    MemberRegistrar memberRegistrar;

    @BeforeEach
    void setUp() {
        memberRegistrar = new MemberRegistrar(memberRepository, oauthRepository, memberRoleRepository,
                foodCategoryManager);
    }

    @Test
    @DisplayName("새 멤버를 등록한다.")
    void registerMember() {
        // given
        NewMember newMember = NewMember.builder()
                .signUpIpAddress("127.0.0.1")
                .memberRoles(new MemberRoles(List.of(MemberRole.ROLE_USER)))
                .email("test@mail.com")
                .build();
        NewOAuthMember newOAuthMember = NewOAuthMember.builder()
                .member(newMember)
                .oauthAccount("account")
                .provider(OAuthProvider.KAKAO)
                .build();
        MemberEntity memberEntity = mock(MemberEntity.class);
        given(memberEntity.getMemberKey()).willReturn("memberKey");
        given(memberRepository.save(any(MemberEntity.class))).willReturn(memberEntity);

        // when
        String registeredMemberKey = memberRegistrar.register(newOAuthMember);

        // then
        assertThat(registeredMemberKey).isEqualTo("memberKey");
    }
}