package com.foodkeeper.foodkeeperserver.food.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FoodCategoryRepository extends JpaRepository<FoodCategoryEntity,Long> {
}
