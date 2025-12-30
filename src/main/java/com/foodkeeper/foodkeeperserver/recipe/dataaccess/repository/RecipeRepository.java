package com.foodkeeper.foodkeeperserver.recipe.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.recipe.dataaccess.entity.RecipeEntity;
import com.foodkeeper.foodkeeperserver.recipe.dataaccess.repository.custom.RecipeCustomRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@NullMarked
public interface RecipeRepository extends JpaRepository<RecipeEntity, Long>, RecipeCustomRepository {
    Optional<RecipeEntity> findByIdAndMemberKey(Long id, String memberKey);
}
