package com.foodkeeper.foodkeeperserver.member.implement;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.MemberRoleEntity;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.OauthEntity;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.MemberRoleRepository;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.OauthRepository;
import com.foodkeeper.foodkeeperserver.auth.domain.enums.MemberRole;
import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.enums.EntityStatus;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodCategoryEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.SelectedFoodCategoryEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.FoodCategoryRepository;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.FoodRepository;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.SelectedFoodCategoryRepository;
import com.foodkeeper.foodkeeperserver.food.domain.StorageMethod;
import com.foodkeeper.foodkeeperserver.member.dataaccess.entity.MemberEntity;
import com.foodkeeper.foodkeeperserver.member.dataaccess.repository.MemberRepository;
import com.foodkeeper.foodkeeperserver.auth.domain.enums.OAuthProvider;
import com.foodkeeper.foodkeeperserver.member.fixture.MemberEntityFixture;
import com.foodkeeper.foodkeeperserver.notification.dataaccess.entity.FcmTokenEntity;
import com.foodkeeper.foodkeeperserver.notification.dataaccess.repository.FcmRepository;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.entity.RecipeEntity;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.entity.RecipeIngredientEntity;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.entity.RecipeStepEntity;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.repository.RecipeIngredientRepository;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.repository.RecipeRepository;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.repository.RecipeStepRepository;
import com.foodkeeper.foodkeeperserver.support.integration.SpringTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MemberWithdrawalProcessorTest extends SpringTest {

    @Autowired MemberWithdrawalProcessor memberWithdrawalProcessor;
    @Autowired MemberRepository memberRepository;
    @Autowired OauthRepository oauthRepository;
    @Autowired FoodRepository foodRepository;
    @Autowired FoodCategoryRepository foodCategoryRepository;
    @Autowired SelectedFoodCategoryRepository selectedFoodCategoryRepository;
    @Autowired RecipeRepository recipeRepository;
    @Autowired RecipeStepRepository recipeStepRepository;
    @Autowired RecipeIngredientRepository recipeIngredientRepository;
    @Autowired FcmRepository fcmRepository;
    @Autowired
    private MemberRoleRepository memberRoleRepository;


    @Test
    @DisplayName("회원 탈퇴 처리를 한다.")
    void withdrawMember() {
        // given
        MemberEntity member = memberRepository.save(MemberEntityFixture.DEFAULT.get());
        MemberRoleEntity memberRole = memberRoleRepository.save(
                new MemberRoleEntity(MemberRole.ROLE_USER, member.getMemberKey()));
        OauthEntity oauth = oauthRepository.save(
                new OauthEntity(OAuthProvider.KAKAO, "account", member.getMemberKey()));
        FoodCategoryEntity category = foodCategoryRepository.save(
                new FoodCategoryEntity("category1", member.getMemberKey()));
        FoodEntity food = foodRepository.save(FoodEntity.builder()
                .name("name")
                .imageUrl("https://test.com/image.jpg")
                .storageMethod(StorageMethod.FROZEN)
                .expiryDate(LocalDate.now().plusDays(5))
                .expiryAlarmDays(2)
                .memo("")
                .selectedCategoryCount(1)
                .memberKey(member.getMemberKey())
                .build());
        SelectedFoodCategoryEntity selectedCategory = selectedFoodCategoryRepository.save(
                new SelectedFoodCategoryEntity(food.getId(), category.getId()));
        RecipeEntity recipe = recipeRepository.save(
                new RecipeEntity("title1", "description", 1, member.getMemberKey()));
        RecipeStepEntity step = recipeStepRepository.save(
                new RecipeStepEntity("title1", "content", recipe.getId()));
        RecipeIngredientEntity ingredient = recipeIngredientRepository.save(
                new RecipeIngredientEntity("name1", "quantity", recipe.getId()));
        FcmTokenEntity fcmToken = fcmRepository.save(new FcmTokenEntity("fcmToken2", member.getMemberKey()));

        // when
        memberWithdrawalProcessor.withdraw(member.getMemberKey());

        // then
        Optional<MemberEntity> foundMember = memberRepository.findById(member.getId());
        Optional<MemberRoleEntity> foundMemberRole = memberRoleRepository.findById(memberRole.getId());
        Optional<OauthEntity> foundOAuth = oauthRepository.findById(oauth.getId());
        Optional<FoodEntity> foundFood = foodRepository.findById(food.getId());
        Optional<FoodCategoryEntity> foundCategory = foodCategoryRepository.findById(category.getId());
        Optional<SelectedFoodCategoryEntity> foundSelectedCategory
                = selectedFoodCategoryRepository.findById(selectedCategory.getId());
        Optional<RecipeEntity> foundRecipe = recipeRepository.findById(recipe.getId());
        Optional<RecipeStepEntity> foundStep = recipeStepRepository.findById(step.getId());
        Optional<RecipeIngredientEntity> foundIngredient = recipeIngredientRepository.findById(ingredient.getId());
        Optional<FcmTokenEntity> foundFcmToken = fcmRepository.findById(fcmToken.getId());

        assertThat(foundMember).isNotEmpty();
        assertThat(foundMemberRole).isNotEmpty();
        assertThat(foundOAuth).isNotEmpty();
        assertThat(foundFood).isNotEmpty();
        assertThat(foundCategory).isNotEmpty();
        assertThat(foundSelectedCategory).isEmpty();
        assertThat(foundRecipe).isNotEmpty();
        assertThat(foundStep).isNotEmpty();
        assertThat(foundIngredient).isNotEmpty();
        assertThat(foundFcmToken).isNotEmpty();

        assertAll(() -> {
            assertThat(foundMember.get().getStatus()).isEqualTo(EntityStatus.DELETED);
            assertThat(foundOAuth.get().getStatus()).isEqualTo(EntityStatus.DELETED);
            assertThat(foundMemberRole.get().getStatus()).isEqualTo(EntityStatus.DELETED);
            assertThat(foundFood.get().getStatus()).isEqualTo(EntityStatus.DELETED);
            assertThat(foundCategory.get().getStatus()).isEqualTo(EntityStatus.DELETED);
            assertThat(foundRecipe.get().getStatus()).isEqualTo(EntityStatus.DELETED);
            assertThat(foundStep.get().getStatus()).isEqualTo(EntityStatus.DELETED);
            assertThat(foundIngredient.get().getStatus()).isEqualTo(EntityStatus.DELETED);
            assertThat(foundFcmToken.get().getStatus()).isEqualTo(EntityStatus.DELETED);
        });
    }
}