package com.foodkeeper.foodkeeperserver.auth.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.OauthEntity;
import com.foodkeeper.foodkeeperserver.auth.domain.enums.OAuthProvider;
import com.foodkeeper.foodkeeperserver.member.dataaccess.entity.MemberEntity;
import com.foodkeeper.foodkeeperserver.member.fixture.MemberEntityFixture;
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
    @DisplayName("email과 provider로 OAuth를 조회한다.")
    void findOAuthByAccount() {
        // given
        OAuthProvider provider = OAuthProvider.KAKAO;
        MemberEntity member = em.persist(MemberEntityFixture.DEFAULT.get());
        em.persist(new OauthEntity(provider, "account", member.getMemberKey()));

        // when
        Optional<OauthEntity> oauth1 = oauthRepository.findByEmail(member.getEmail(), provider);
        Optional<OauthEntity> oauth2 = oauthRepository.findByEmail("another@mail.com", provider);

        // then
        assertThat(oauth1).isNotEmpty();
        assertThat(oauth2).isEmpty();
        assertThat(oauth1.get().getAccount()).isEqualTo("account");
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