package com.foodkeeper.foodkeeperserver.food.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodCategoryEntity;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

@NullMarked
public interface FoodCategoryRepository extends JpaRepository<FoodCategoryEntity,Long> {
}
