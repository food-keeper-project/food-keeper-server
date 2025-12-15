package com.foodkeeper.foodkeeperserver.food.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.custom.FoodRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FoodRepository extends JpaRepository<FoodEntity,Long>, FoodRepositoryCustom {
}
