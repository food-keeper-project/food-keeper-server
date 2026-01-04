package com.foodkeeper.foodkeeperserver.recipe.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.recipe.dataaccess.entity.RecipeStepEntity;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.repository.custom.RecipeStepCustomRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@NullMarked
public interface RecipeStepRepository extends JpaRepository<RecipeStepEntity, Long>, RecipeStepCustomRepository {
    List<RecipeStepEntity> findByRecipeId(Long recipeId);
}
