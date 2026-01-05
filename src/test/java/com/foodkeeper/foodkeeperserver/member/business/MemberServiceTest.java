package com.foodkeeper.foodkeeperserver.member.business;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.MemberRoleRepository;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.OauthRepository;
import com.foodkeeper.foodkeeperserver.food.implement.CategoryManager;
import com.foodkeeper.foodkeeperserver.food.implement.FoodManager;
import com.foodkeeper.foodkeeperserver.member.dataaccess.entity.MemberEntity;
import com.foodkeeper.foodkeeperserver.member.dataaccess.repository.MemberRepository;
import com.foodkeeper.foodkeeperserver.member.domain.Member;
import com.foodkeeper.foodkeeperserver.member.implement.MemberFinder;
import com.foodkeeper.foodkeeperserver.member.implement.MemberWithdrawalProcessor;
import com.foodkeeper.foodkeeperserver.notification.implement.FcmManager;
import com.foodkeeper.foodkeeperserver.recipe.implement.RecipeManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock MemberRepository memberRepository;
    @Mock MemberRoleRepository memberRoleRepository;
    @Mock OauthRepository oauthRepository;
    @Mock FoodManager foodManager;
    @Mock CategoryManager categoryManager;
    @Mock RecipeManager recipeManager;
    @Mock FcmManager fcmManager;
    MemberService memberService;

    @BeforeEach
    void setUp() {
        MemberFinder memberFinder = new MemberFinder(memberRepository);
        MemberWithdrawalProcessor memberWithdrawalProcessor = new MemberWithdrawalProcessor(memberRepository,
                memberRoleRepository, oauthRepository, foodManager, categoryManager, recipeManager, fcmManager);
        memberService = new MemberService(memberFinder, memberWithdrawalProcessor);
    }

    @Test
    @DisplayName("멤버 Key를 통해 멤버를 조회한다.")
    void findMemberByKey() {
        // given
        String memberKey = "memberKey";
        MemberEntity memberEntity = mock(MemberEntity.class);
        Member member = Member.builder().memberKey(memberKey).build();
        given(memberRepository.findByMemberKey(memberKey)).willReturn(Optional.of(memberEntity));
        given(memberEntity.toDomain()).willReturn(member);

        // when
        Member foundMember = memberService.findMember(memberKey);

        // then
        assertThat(foundMember.memberKey()).isEqualTo("memberKey");
    }


}