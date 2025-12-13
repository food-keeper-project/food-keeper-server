package com.foodkeeper.foodkeeperserver.food.repository;

import com.foodkeeper.foodkeeperserver.food.entity.FoodCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodCategoryRepository extends JpaRepository<FoodCategory,Long> {
}
