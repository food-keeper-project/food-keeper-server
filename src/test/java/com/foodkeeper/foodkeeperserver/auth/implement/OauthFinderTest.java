package com.foodkeeper.foodkeeperserver.auth.implement;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.OauthEntity;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.OauthRepository;
import com.foodkeeper.foodkeeperserver.member.domain.enums.OAuthProvider;
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
    @DisplayName("OAuth Account와 일치하는 멤버의 멤버키를 조회한다.")
    void findMemberKeyByOAuthAccount() {
        // given
        String oauthAccount = "oauthAccount";
        OauthEntity oauthEntity = new OauthEntity(OAuthProvider.KAKAO, oauthAccount, "memberKey");
        given(oauthRepository.findByAccount(eq(oauthAccount))).willReturn(Optional.of(oauthEntity));

        // when
        String memberKey = oauthFinder.findMemberKeyByOAuthAccount(oauthAccount).orElse(null);

        // then
        assertThat(memberKey).isEqualTo("memberKey");
    }
}