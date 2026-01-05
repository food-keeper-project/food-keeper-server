package com.foodkeeper.foodkeeperserver.notification.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.enums.EntityStatus;
import com.foodkeeper.foodkeeperserver.notification.dataaccess.entity.FcmTokenEntity;
import com.foodkeeper.foodkeeperserver.support.repository.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class FcmRepositoryTest extends RepositoryTest {

    @Autowired FcmRepository fcmRepository;

    @Test
    @DisplayName("memberKey로 해당 Member의 FcmToken들을 삭제한다.")
    void deleteFcmTokensByMemberKey() {
        // given
        String memberKey1 = "memberKey1";
        FcmTokenEntity fcmToken1 = em.persist(new FcmTokenEntity("fcmToken1", memberKey1));
        FcmTokenEntity fcmToken2 = em.persist(new FcmTokenEntity("fcmToken2", memberKey1));
        FcmTokenEntity fcmToken3 = em.persist(new FcmTokenEntity("fcmToken3", "memberKey2"));

        // when
        fcmRepository.deleteFcmTokens(memberKey1);

        // then
        Optional<FcmTokenEntity> foundToken1 = fcmRepository.findByToken(fcmToken1.getToken());
        Optional<FcmTokenEntity> foundToken2 = fcmRepository.findByToken(fcmToken2.getToken());
        Optional<FcmTokenEntity> foundToken3 = fcmRepository.findByToken(fcmToken3.getToken());
        assertThat(foundToken1).isEmpty();
        assertThat(foundToken2).isEmpty();
        assertThat(foundToken3).isNotEmpty();
    }

    @Test
    @DisplayName("memberKey에 해당하는 FcmToken들을 조회한다.")
    void findFcmTokensByMemberKeys() {
        FcmTokenEntity fcmToken1 = em.persist(new FcmTokenEntity("fcmToken1", "memberKey1"));
        FcmTokenEntity fcmToken2 = em.persist(new FcmTokenEntity("fcmToken2", "memberKey2"));
        em.persist(new FcmTokenEntity("fcmToken3", "memberKey3"));

        // when
        List<FcmTokenEntity> tokens = fcmRepository.findAllByMemberKeyIn(Set.of("memberKey1", "memberKey2"));

        // then
        assertThat(tokens).hasSize(2);
        assertThat(tokens).containsExactly(fcmToken1, fcmToken2);
    }

    @Test
    @DisplayName("token값과 일치하는 fcmToken을 조회한다.")
    void findByToken() {
        em.persist(new FcmTokenEntity("fcmToken1", "memberKey1"));
        em.persist(new FcmTokenEntity("fcmToken2", "memberKey2"));

        // when
        Optional<FcmTokenEntity> foundFcmToken = fcmRepository.findByToken("fcmToken2");

        // then
        assertThat(foundFcmToken).isNotEmpty();
        assertThat(foundFcmToken.get().getToken()).isEqualTo("fcmToken2");
    }

    @Test
    @DisplayName("token값과 일치하는 fcmToken을 삭제한다.")
    void deleteByToken() {
        em.persist(new FcmTokenEntity("fcmToken1", "memberKey1"));
        FcmTokenEntity fcmToken2 = em.persist(new FcmTokenEntity("fcmToken2", "memberKey2"));

        // when
        fcmRepository.deleteByToken("fcmToken2");
        em.flush();
        em.clear();

        // then
        Optional<FcmTokenEntity> foundFcmToken = fcmRepository.findById(fcmToken2.getId());
        assertThat(foundFcmToken).isNotEmpty();
        assertThat(foundFcmToken.get().getStatus()).isEqualTo(EntityStatus.DELETED);
    }
}