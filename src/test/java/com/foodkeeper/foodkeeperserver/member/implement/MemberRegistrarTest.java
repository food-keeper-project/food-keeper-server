package com.foodkeeper.foodkeeperserver.member.implement;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.MemberRoleRepository;
import com.foodkeeper.foodkeeperserver.auth.domain.MemberRoles;
import com.foodkeeper.foodkeeperserver.auth.domain.enums.MemberRole;
import com.foodkeeper.foodkeeperserver.food.implement.CategoryManager;
import com.foodkeeper.foodkeeperserver.member.dataaccess.entity.MemberEntity;
import com.foodkeeper.foodkeeperserver.member.dataaccess.repository.MemberRepository;
import com.foodkeeper.foodkeeperserver.member.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class MemberRegistrarTest {

    @Mock MemberRepository memberRepository;
    @Mock MemberRoleRepository memberRoleRepository;
    @Mock CategoryManager foodCategoryManager;
    MemberRegistrar memberRegistrar;

    @BeforeEach
    void setUp() {
        memberRegistrar = new MemberRegistrar(memberRepository, memberRoleRepository, foodCategoryManager);
    }

    @Test
    @DisplayName("새 멤버를 등록한다.")
    void registerMember() {
        // given
        NewMember newMember = NewMember.builder()
                .ipAddress(new IpAddress("127.0.0.1"))
                .memberRoles(new MemberRoles(List.of(MemberRole.ROLE_USER)))
                .email(new Email("test@mail.com"))
                .nickname(new Nickname("nickname"))
                .imageUrl(new ProfileImageUrl("https://test.com/image.jpg"))
                .build();
        MemberEntity memberEntity = mock(MemberEntity.class);
        given(memberEntity.getMemberKey()).willReturn("memberKey");
        given(memberRepository.save(any(MemberEntity.class))).willReturn(memberEntity);

        // when
        String registeredMemberKey = memberRegistrar.register(newMember);

        // then
        assertThat(registeredMemberKey).isEqualTo("memberKey");
    }
}