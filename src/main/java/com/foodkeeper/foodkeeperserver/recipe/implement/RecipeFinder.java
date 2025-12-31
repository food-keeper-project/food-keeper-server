package com.foodkeeper.foodkeeperserver.recipe.implement;

import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.foodkeeper.foodkeeperserver.common.domain.SliceObject;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.entity.RecipeEntity;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.entity.RecipeIngredientEntity;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.entity.RecipeStepEntity;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.repository.RecipeIngredientRepository;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.repository.RecipeRepository;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.repository.RecipeStepRepository;
import com.foodkeeper.foodkeeperserver.recipe.domain.Recipe;
import com.foodkeeper.foodkeeperserver.recipe.domain.RecipeIngredient;
import com.foodkeeper.foodkeeperserver.recipe.domain.RecipeStep;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RecipeFinder {

    private final RecipeRepository recipeRepository;
    private final RecipeStepRepository recipeStepRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;

    public SliceObject<Recipe> findPaged(Cursorable<Long> cursorable, String memberKey) {
        return recipeRepository.findRecipes(cursorable, memberKey).map(RecipeEntity::toDomain);
    }

    public Recipe find(Long recipeId, String memberKey) {
        RecipeEntity recipeEntity = recipeRepository.findByIdAndMemberKey(recipeId, memberKey)
                .orElseThrow(() -> new AppException(ErrorType.DEFAULT_ERROR));

        List<RecipeStep> recipeSteps = recipeStepRepository.findByRecipeId(recipeId).stream()
                .map(RecipeStepEntity::toDomain)
                .toList();
        List<RecipeIngredient> recipeIngredients = recipeIngredientRepository.findByRecipeId(recipeId).stream()
                .map(RecipeIngredientEntity::toDomain)
                .toList();

        return recipeEntity.toDomain(recipeSteps, recipeIngredients);
    }
}
