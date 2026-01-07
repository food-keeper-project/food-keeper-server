package com.foodkeeper.foodkeeperserver.auth.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.MemberRoleEntity;
import com.foodkeeper.foodkeeperserver.auth.domain.enums.MemberRole;
import com.foodkeeper.foodkeeperserver.member.dataaccess.entity.MemberEntity;
import com.foodkeeper.foodkeeperserver.member.fixture.MemberEntityFixture;
import com.foodkeeper.foodkeeperserver.support.repository.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MemberRoleRepositoryTest extends RepositoryTest {

    @Autowired MemberRoleRepository memberRoleRepository;

    @Test
    @DisplayName("MemberRole을 조회한다.")
    void findMemberRole() {
        // given
        MemberEntity member = em.persist(MemberEntityFixture.DEFAULT.get());
        em.persist(new MemberRoleEntity(MemberRole.ROLE_USER, member.getMemberKey()));
        em.persist(new MemberRoleEntity(MemberRole.ROLE_USER, "anotherMemberKey"));

        // when
        List<MemberRoleEntity> memberRoles = memberRoleRepository.findByMemberKey(member.getMemberKey());

        // then
        assertThat(memberRoles).hasSize(1);
        assertThat(memberRoles.getFirst().getRole()).isEqualTo(MemberRole.ROLE_USER);
        assertThat(memberRoles.getFirst().getMemberKey()).isEqualTo(member.getMemberKey());
    }
}