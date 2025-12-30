package com.foodkeeper.foodkeeperserver.recipe.implement;

import com.foodkeeper.foodkeeperserver.recipe.dataaccess.entity.RecipeEntity;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.entity.RecipeIngredientEntity;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.entity.RecipeStepEntity;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.repository.RecipeIngredientRepository;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.repository.RecipeRepository;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.repository.RecipeStepRepository;
import com.foodkeeper.foodkeeperserver.recipe.domain.NewRecipe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RecipeRegistrar {

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
}
