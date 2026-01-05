package com.foodkeeper.foodkeeperserver.auth.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.OauthEntity;
import com.foodkeeper.foodkeeperserver.member.domain.enums.OAuthProvider;
import com.foodkeeper.foodkeeperserver.support.repository.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class OauthRepositoryTest extends RepositoryTest {

    @Autowired OauthRepository oauthRepository;

    @Test
    @DisplayName("account로 OAuth를 조회한다.")
    void findOAuthByAccount() {
        // given
        String account = "account";
        em.persist(new OauthEntity(OAuthProvider.KAKAO, account, "memberKey"));

        // when
        Optional<OauthEntity> oauth = oauthRepository.findByAccount(account);

        // then
        assertThat(oauth).isNotEmpty();
        assertThat(oauth.get().getAccount()).isEqualTo(account);
    }

    @Test
    @DisplayName("memberKey로 해당 멤버의 모든 OAuth를 조회한다.")
    void findOAuthsByMemberKey() {
        // given
        String memberKey = "memberKey1";
        em.persist(new OauthEntity(OAuthProvider.KAKAO, "account1", "memberKey1"));
        em.persist(new OauthEntity(OAuthProvider.KAKAO, "account2", "memberKey1"));
        em.persist(new OauthEntity(OAuthProvider.KAKAO, "account3", "memberKey2"));

        // when
        List<OauthEntity> oauths = oauthRepository.findAllByMemberKey(memberKey);

        // then
        assertThat(oauths).hasSize(2);
    }
}