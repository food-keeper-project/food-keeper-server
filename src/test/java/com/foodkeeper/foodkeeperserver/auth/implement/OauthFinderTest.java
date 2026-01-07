package com.foodkeeper.foodkeeperserver.auth.implement;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.OauthEntity;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.OauthRepository;
import com.foodkeeper.foodkeeperserver.auth.domain.enums.OAuthProvider;
import com.foodkeeper.foodkeeperserver.member.domain.Email;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OauthFinderTest {

    @Mock OauthRepository oauthRepository;
    @InjectMocks OauthFinder oauthFinder;

    @Test
    @DisplayName("OAuth Account, Provider 와 일치하는 멤버의 멤버키를 조회한다.")
    void findMemberKey() {
        // given
        String email = "test@mail.com";
        OAuthProvider provider = OAuthProvider.KAKAO;
        OauthEntity oauthEntity = new OauthEntity(provider, "oauthAccount", "memberKey");
        given(oauthRepository.findByEmail(eq(email), eq(provider))).willReturn(Optional.of(oauthEntity));

        // when
        String memberKey = oauthFinder.findMemberKey(new Email(email), provider).orElse(null);

        // then
        assertThat(memberKey).isEqualTo("memberKey");
    }
}