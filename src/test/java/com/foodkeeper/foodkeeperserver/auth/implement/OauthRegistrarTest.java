package com.foodkeeper.foodkeeperserver.auth.implement;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.OauthEntity;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.OauthRepository;
import com.foodkeeper.foodkeeperserver.auth.domain.MemberRoles;
import com.foodkeeper.foodkeeperserver.auth.domain.enums.MemberRole;
import com.foodkeeper.foodkeeperserver.auth.domain.enums.OAuthProvider;
import com.foodkeeper.foodkeeperserver.member.domain.*;
import com.foodkeeper.foodkeeperserver.member.implement.MemberRegistrar;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OauthRegistrarTest {

    @Mock OauthRepository oauthRepository;
    @Mock MemberRegistrar memberRegistrar;
    @InjectMocks OauthRegistrar oauthRegistrar;

    @Test
    @DisplayName("Email, Provider 데이터 조회 후 있으면 memberKey를 반환한다.")
    void returnMemberKeyIfEmailAndProviderExists() {
        // given
        String email = "test@mail.com";
        OAuthProvider provider = OAuthProvider.KAKAO;
        String memberKey = "memberKey";
        OauthEntity oauthEntity = new OauthEntity(provider, "oauthAccount", memberKey);
        NewMember newMember = NewMember.builder()
                .ipAddress(new IpAddress("127.0.0.1"))
                .memberRoles(new MemberRoles(List.of(MemberRole.ROLE_USER)))
                .email(new Email(email))
                .nickname(new Nickname("nickname"))
                .imageUrl(new ProfileImageUrl("https://test.com/image.jpg"))
                .build();
        NewOAuthMember newOAuthMember = NewOAuthMember.builder()
                .member(newMember)
                .oauthAccount("account")
                .provider(provider)
                .build();
        given(oauthRepository.findByEmail(eq(email), eq(provider))).willReturn(Optional.of(oauthEntity));

        // when
        String foundMemberKey = oauthRegistrar.registerIfNewAndGetMemberKey(newOAuthMember);

        // then
        assertThat(foundMemberKey).isEqualTo(memberKey);
    }

    @Test
    @DisplayName("Email, Provider 데이터 조회 후 없으면 등록하여 memberKey를 반환한다.")
    void findMemberKeyAfterRegisterIfNotExistsAndReturnMemberKey() {
        // given
        String memberKey = "memberKey";
        NewMember newMember = NewMember.builder()
                .ipAddress(new IpAddress("127.0.0.1"))
                .memberRoles(new MemberRoles(List.of(MemberRole.ROLE_USER)))
                .email(new Email("test@mail.com"))
                .nickname(new Nickname("nickname"))
                .imageUrl(new ProfileImageUrl("https://test.com/image.jpg"))
                .build();
        NewOAuthMember newOAuthMember = NewOAuthMember.builder()
                .member(newMember)
                .oauthAccount("account")
                .provider(OAuthProvider.KAKAO)
                .build();
        given(oauthRepository.findByEmail(any(), any())).willReturn(Optional.empty());
        given(memberRegistrar.register(eq(newMember))).willReturn(memberKey);

        // when
        String foundMemberKey = oauthRegistrar.registerIfNewAndGetMemberKey(newOAuthMember);

        // then
        assertThat(foundMemberKey).isEqualTo(memberKey);
    }
}