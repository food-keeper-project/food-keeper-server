package com.foodkeeper.foodkeeperserver.recipe.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.recipe.dataaccess.entity.RecipeEntity;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

@NullMarked
public interface RecipeRepository extends JpaRepository<RecipeEntity, Long> {
}
