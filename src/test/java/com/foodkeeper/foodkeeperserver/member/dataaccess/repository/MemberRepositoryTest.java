package com.foodkeeper.foodkeeperserver.member.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.member.dataaccess.entity.MemberEntity;
import com.foodkeeper.foodkeeperserver.member.fixture.MemberEntityFixture;
import com.foodkeeper.foodkeeperserver.support.repository.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class MemberRepositoryTest extends RepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    @DisplayName("memberKey로 해당 Member를 찾는다.")
    void findByMemberKey() {
        // given
        MemberEntity member1 = em.persist(MemberEntityFixture.DEFAULT.get());
        em.persist(MemberEntityFixture.DEFAULT.get());

        // when
        Optional<MemberEntity> foundMember = memberRepository.findByMemberKey(member1.getMemberKey());

        // then
        assertThat(foundMember).isNotEmpty();
        assertThat(foundMember.get()).isEqualTo(member1);
    }

    @Test
    @DisplayName("memberKey로 해당 Member의 refreshToken을 찾는다.")
    void findRefreshTokenByMemberKey() {
        // given
        String refreshToken = "refresh";
        MemberEntity member = em.persist(MemberEntityFixture.DEFAULT.get());
        em.persist(MemberEntityFixture.DEFAULT.get());
        memberRepository.updateRefreshToken(member.getMemberKey(), refreshToken);
        em.flush();
        em.clear();

        // when
        Optional<String> foundRefreshToken = memberRepository.findRefreshToken(member.getMemberKey());

        // then
        assertThat(foundRefreshToken).isNotEmpty();
        assertThat(foundRefreshToken.get()).isEqualTo(refreshToken);
    }

    @Test
    @DisplayName("Member의 refreshToken을 갱신한다.")
    void updateRefreshToken() {
        // given
        String newRefreshToken = "newRefreshToken";
        MemberEntity member = em.persist(MemberEntityFixture.DEFAULT.get());

        // when
        memberRepository.updateRefreshToken(member.getMemberKey(), newRefreshToken);
        em.flush();
        em.clear();

        // then
        Optional<MemberEntity> foundMember = memberRepository.findById(member.getId());
        assertThat(foundMember).isNotEmpty();
        assertThat(foundMember.get().getRefreshToken()).isEqualTo(newRefreshToken);
    }

    @Test
    @DisplayName("Member의 RefreshToken을 삭제한다.")
    void deleteRefreshToken() {
        // given
        MemberEntity member = em.persist(MemberEntityFixture.DEFAULT.get());

        // when
        memberRepository.deleteRefreshTokenByMemberKey(member.getMemberKey());

        // then
        Optional<MemberEntity> foundMember = memberRepository.findById(member.getId());
        assertThat(foundMember).isNotEmpty();
        assertThat(foundMember.get().getRefreshToken()).isNull();
    }

    @Test
    @DisplayName("Member의 email이 존재하면 true를 반환한다.")
    void returnTrueIfMemberEmailExists() {
        // given
        MemberEntity member = em.persist(MemberEntityFixture.DEFAULT.get());

        // when
        boolean exists = memberRepository.existsByEmail(member.getEmail());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Member의 email이 존재하지 않으면 false를 반환한다.")
    void returnFalseIfMemberEmailExists() {
        // given
        em.persist(MemberEntityFixture.DEFAULT.get());

        // when
        boolean exists = memberRepository.existsByEmail("anotherEmail");

        // then
        assertThat(exists).isFalse();
    }
}