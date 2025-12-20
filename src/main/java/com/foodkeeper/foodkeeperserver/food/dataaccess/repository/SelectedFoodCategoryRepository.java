package com.foodkeeper.foodkeeperserver.food.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.SelectedFoodCategoryEntity;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@NullMarked
public interface SelectedFoodCategoryRepository extends JpaRepository<SelectedFoodCategoryEntity, Long> {
    List<SelectedFoodCategoryEntity> findByFoodIdIn(List<Long> foodIds);
    List<SelectedFoodCategoryEntity> findByFoodId(Long foodId);
    void deleteAllByFoodId(Long foodId);
}
