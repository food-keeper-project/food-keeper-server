package com.foodkeeper.foodkeeperserver.recipe.business;

import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.foodkeeper.foodkeeperserver.common.domain.SliceObject;
import com.foodkeeper.foodkeeperserver.recipe.domain.NewRecipe;
import com.foodkeeper.foodkeeperserver.recipe.domain.Recipe;
import com.foodkeeper.foodkeeperserver.ai.implement.AiRecipeRecommender;
import com.foodkeeper.foodkeeperserver.recipe.implement.RecipeFinder;
import com.foodkeeper.foodkeeperserver.recipe.implement.RecipeManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final AiRecipeRecommender aiRecipeRecommender;
    private final RecipeManager recipeManager;
    private final RecipeFinder recipeFinder;

    public SliceObject<Recipe> findRecipes(Cursorable<Long> cursorable, String memberKey) {
        return recipeFinder.findPaged(cursorable, memberKey);
    }

    public Recipe findRecipe(Long recipeId, String memberKey) {
        return recipeFinder.find(recipeId, memberKey);
    }

    public Long registerRecipe(NewRecipe recipe, String memberKey) {
        return recipeManager.register(recipe, memberKey);
    }

    public long recipeCount(String memberKey) {
        return recipeFinder.recipeCount(memberKey);
    }

    public NewRecipe recommendRecipe(List<String> ingredients, List<String> excludedMenus) {
        return aiRecipeRecommender.getRecipeRecommendation(ingredients, excludedMenus);
    }

    public void removeRecipe(Long recipeId, String memberKey) {
        recipeManager.remove(recipeId, memberKey);
    }
}
