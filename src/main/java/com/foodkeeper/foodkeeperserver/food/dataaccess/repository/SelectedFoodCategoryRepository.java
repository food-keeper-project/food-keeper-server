package com.foodkeeper.foodkeeperserver.food.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.SelectedFoodCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SelectedFoodCategoryRepository extends JpaRepository<SelectedFoodCategoryEntity, Long> {
    List<SelectedFoodCategoryEntity> findByFoodIds(List<Long> foodIds);
    List<SelectedFoodCategoryEntity> findByFoodId(Long foodId);
}
