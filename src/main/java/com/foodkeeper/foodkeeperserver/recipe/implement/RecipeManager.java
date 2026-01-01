package com.foodkeeper.foodkeeperserver.recipe.implement;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.BaseEntity;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.entity.RecipeEntity;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.entity.RecipeIngredientEntity;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.entity.RecipeStepEntity;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.repository.RecipeIngredientRepository;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.repository.RecipeRepository;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.repository.RecipeStepRepository;
import com.foodkeeper.foodkeeperserver.recipe.domain.NewRecipe;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RecipeManager {

    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RecipeStepRepository recipeStepRepository;

    @Transactional
    public Long register(NewRecipe recipe, String memberKey) {
        RecipeEntity recipeEntity = recipeRepository.save(RecipeEntity.of(recipe, memberKey));
        recipe.ingredients().forEach(ingredient ->
                recipeIngredientRepository.save(RecipeIngredientEntity.of(ingredient, recipeEntity.getId())));
        recipe.steps().forEach(recipeStep ->
                recipeStepRepository.save(RecipeStepEntity.of(recipeStep, recipeEntity.getId())));

        return recipeEntity.getId();
    }

    @Transactional
    public void remove(Long recipeId, String memberKey) {
        RecipeEntity recipeEntity = recipeRepository.findByIdAndMemberKey(recipeId, memberKey)
                .orElseThrow(() -> new AppException(ErrorType.RECIPE_DATA_NOT_FOUND));

        recipeIngredientRepository.findByRecipeId(recipeId).forEach(BaseEntity::delete);
        recipeStepRepository.findByRecipeId(recipeId).forEach(BaseEntity::delete);
        recipeEntity.delete();
    }

    @Transactional
    public void removeRecipes(String memberKey) {
        List<Long> recipeIds = recipeRepository.deleteRecipes(memberKey);

        recipeIngredientRepository.deleteAllByRecipeIds(recipeIds);
        recipeStepRepository.deleteAllByRecipeIds(recipeIds);
    }
}
