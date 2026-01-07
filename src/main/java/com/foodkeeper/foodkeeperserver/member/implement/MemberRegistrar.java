package com.foodkeeper.foodkeeperserver.member.implement;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.LocalAuthEntity;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.MemberRoleEntity;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.OauthEntity;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.LocalAuthRepository;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.MemberRoleRepository;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.OauthRepository;
import com.foodkeeper.foodkeeperserver.food.implement.CategoryManager;
import com.foodkeeper.foodkeeperserver.member.dataaccess.entity.MemberEntity;
import com.foodkeeper.foodkeeperserver.member.dataaccess.repository.MemberRepository;
import com.foodkeeper.foodkeeperserver.member.domain.NewLocalMember;
import com.foodkeeper.foodkeeperserver.member.domain.NewOAuthMember;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MemberRegistrar {
    private final MemberRepository memberRepository;
    private final OauthRepository oauthRepository;
    private final LocalAuthRepository localAuthRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final CategoryManager foodCategoryManager;

    @Transactional
    public String register(NewOAuthMember newOAuthMember) {
        if (memberRepository.existsByEmail(newOAuthMember.member().getEmail())) {
            throw new AppException(ErrorType.INVALID_EMAIL);
        }
        MemberEntity memberEntity = memberRepository.save(MemberEntity.from(newOAuthMember.member()));
        oauthRepository.save(
                new OauthEntity(newOAuthMember.provider(), newOAuthMember.oauthAccount(), memberEntity.getMemberKey()));
        newOAuthMember.member().memberRoles().forEach(role ->
                memberRoleRepository.save(new MemberRoleEntity(role, memberEntity.getMemberKey())));

        foodCategoryManager.registerDefaultCategories(memberEntity.getMemberKey());

        return memberEntity.getMemberKey();
    }

    @Transactional
    public String register(NewLocalMember newLocalMember) {
        MemberEntity memberEntity = memberRepository.save(MemberEntity.from(newLocalMember.member()));
        localAuthRepository.save(
                new LocalAuthEntity(newLocalMember.getAccount(), newLocalMember.getPassword(), memberEntity.getMemberKey()));
        newLocalMember.member().memberRoles().forEach(role ->
                memberRoleRepository.save(new MemberRoleEntity(role, memberEntity.getMemberKey())));

        foodCategoryManager.registerDefaultCategories(memberEntity.getMemberKey());

        return memberEntity.getMemberKey();
    }
}
