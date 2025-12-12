package com.foodkeeper.foodkeeperserver.domain.food.repository;

import com.foodkeeper.foodkeeperserver.domain.food.entity.SelectedFoodCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelectedFoodCategoryRepository extends JpaRepository<SelectedFoodCategory,Long> {

}
