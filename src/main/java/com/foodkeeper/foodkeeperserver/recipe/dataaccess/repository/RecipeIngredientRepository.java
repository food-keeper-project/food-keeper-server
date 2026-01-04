package com.foodkeeper.foodkeeperserver.recipe.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.recipe.dataaccess.entity.RecipeIngredientEntity;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.repository.custom.RecipeIngredientCustomRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@NullMarked
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredientEntity, Long>,
        RecipeIngredientCustomRepository {
    List<RecipeIngredientEntity> findByRecipeId(Long recipeId);
}
