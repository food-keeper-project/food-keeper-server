package com.foodkeeper.foodkeeperserver.recipe.business;

import com.foodkeeper.foodkeeperserver.recipe.domain.Recipe;
import com.foodkeeper.foodkeeperserver.recipe.implement.AiRecipeRecommender;
import com.foodkeeper.foodkeeperserver.recipe.implement.RecipeRegistrar;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final AiRecipeRecommender aiRecipeRecommender;
    private final RecipeRegistrar recipeRegistrar;

    public Long registerRecipe(Recipe recipe, String memberKey) {
        return recipeRegistrar.register(recipe, memberKey);
    }

    public Recipe recommendRecipe(List<String> ingredients, List<String> excludedMenus) {
        return aiRecipeRecommender.getRecipeRecommendation(ingredients, excludedMenus);
    }
}
