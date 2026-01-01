package com.foodkeeper.foodkeeperserver.food.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.food.dataaccess.repository.custom.FoodRepositoryCustom;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@NullMarked
public interface FoodRepository extends JpaRepository<FoodEntity, Long>, FoodRepositoryCustom {
    Optional<FoodEntity> findByIdAndMemberKey(Long id, String memberKey);
}
