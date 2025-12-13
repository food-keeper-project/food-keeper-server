package com.foodkeeper.foodkeeperserver.food.repository;

import com.foodkeeper.foodkeeperserver.food.entity.SelectedFoodCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface SelectedFoodCategoryRepository extends JpaRepository<SelectedFoodCategory, Long> {
}
