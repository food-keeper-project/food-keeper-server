package com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.LocalAuthEntity;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.LocalAuthRepository;
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
}