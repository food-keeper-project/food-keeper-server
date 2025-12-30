package com.foodkeeper.foodkeeperserver.recipe.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.recipe.dataaccess.entity.RecipeStepEntity;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

@NullMarked
public interface RecipeStepRepository extends JpaRepository<RecipeStepEntity, Long> {
}
