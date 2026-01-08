package com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.LocalAuthEntity;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.LocalAuthRepository;
import com.foodkeeper.foodkeeperserver.member.dataaccess.entity.MemberEntity;
import com.foodkeeper.foodkeeperserver.member.fixture.MemberEntityFixture;
import com.foodkeeper.foodkeeperserver.support.repository.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class LocalAuthRepositoryTest extends RepositoryTest {

    @Autowired
    LocalAuthRepository localAuthRepository;

    @Test
    @DisplayName("account가 존재하면 true가 반환된다.")
    void returnTrueIfAccountExists() {
        // given
        String account = "account";
        em.persist(new LocalAuthEntity("account", "password", "memberKey"));

        // when
        boolean exists = localAuthRepository.existsByAccount(account);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("account가 존재하지 않으면 false가 반환된다.")
    void returnFalseIfAccountNotExists() {
        // given
        String account = "account";
        em.persist(new LocalAuthEntity("anotherAccount", "password", "memberKey"));

        // when
        boolean exists = localAuthRepository.existsByAccount(account);

        // then
        assertThat(exists).isFalse();
    }


    @Test
    @DisplayName("Local로 가입한 Member의 email이 존재하면 true를 반환한다.")
    void returnTrueIfMemberEmailExists() {
        // given
        MemberEntity member = em.persist(MemberEntityFixture.DEFAULT.get());
        em.persist(new LocalAuthEntity("account", "password", member.getMemberKey()));

        // when
        boolean exists = localAuthRepository.existsByEmail(member.getEmail());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Local로 가입한 Member의 email이 존재하지 않으면 false를 반환한다.")
    void returnFalseIfMemberEmailExists() {
        // given
        MemberEntity member = em.persist(MemberEntityFixture.DEFAULT.get());
        em.persist(new LocalAuthEntity("account", "password", member.getMemberKey()));

        // when
        boolean exists = localAuthRepository.existsByEmail("anotherEmail");

        // then
        assertThat(exists).isFalse();
    }
}