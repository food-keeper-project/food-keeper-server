package com.foodkeeper.foodkeeperserver.member.implement;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.MemberRoleRepository;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.OauthRepository;
import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.BaseEntity;
import com.foodkeeper.foodkeeperserver.food.implement.CategoryManager;
import com.foodkeeper.foodkeeperserver.food.implement.FoodManager;
import com.foodkeeper.foodkeeperserver.member.dataaccess.repository.MemberRepository;
import com.foodkeeper.foodkeeperserver.recipe.implement.RecipeManager;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MemberWithdrawalProcessor {
    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final OauthRepository oauthRepository;
    private final FoodManager foodManager;
    private final CategoryManager foodCategoryManager;
    private final RecipeManager recipeManager;

    @Transactional
    public void withdraw(String memberKey) {
        memberRepository.findByMemberKey(memberKey).orElseThrow(() -> new AppException(ErrorType.NOT_FOUND_DATA))
                .delete();
        memberRoleRepository.findByMemberKey(memberKey).forEach(BaseEntity::delete);
        oauthRepository.findAllByMemberKey(memberKey).forEach(BaseEntity::delete);

        foodManager.removeFoods(memberKey);
        recipeManager.removeRecipes(memberKey);
        foodCategoryManager.removeCategories(memberKey);
    }
}
