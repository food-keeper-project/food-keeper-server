package com.foodkeeper.foodkeeperserver.food.dataaccess.repository;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodCategoryEntity;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@NullMarked
public interface FoodCategoryRepository extends JpaRepository<FoodCategoryEntity, Long> {

    List<FoodCategoryEntity> findAllByMemberKey(String memberKey);

    Optional<FoodCategoryEntity> findByIdAndMemberKey(Long id, String memberKey);

    List<FoodCategoryEntity> findAllByIdIn(List<Long> ids);
}
