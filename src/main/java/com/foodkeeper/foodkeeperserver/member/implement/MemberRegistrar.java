package com.foodkeeper.foodkeeperserver.member.implement;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.MemberRoleEntity;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.MemberRoleRepository;
import com.foodkeeper.foodkeeperserver.food.implement.CategoryManager;
import com.foodkeeper.foodkeeperserver.member.dataaccess.entity.MemberEntity;
import com.foodkeeper.foodkeeperserver.member.dataaccess.repository.MemberRepository;
import com.foodkeeper.foodkeeperserver.member.domain.NewMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MemberRegistrar {
    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final CategoryManager foodCategoryManager;

    @Transactional
    public String register(NewMember newMember) {
        MemberEntity memberEntity = memberRepository.save(MemberEntity.from(newMember));
        newMember.memberRoles().forEach(role ->
                memberRoleRepository.save(new MemberRoleEntity(role, memberEntity.getMemberKey())));

        foodCategoryManager.registerDefaultCategories(memberEntity.getMemberKey());

        return memberEntity.getMemberKey();
    }
}
