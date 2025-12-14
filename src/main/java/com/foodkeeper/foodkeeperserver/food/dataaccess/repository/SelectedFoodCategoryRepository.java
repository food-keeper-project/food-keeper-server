package com.foodkeeper.foodkeeperserver.food.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.SelectedFoodCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SelectedFoodCategoryRepository extends JpaRepository<SelectedFoodCategoryEntity, Long> {
}
