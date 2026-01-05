package com.foodkeeper.foodkeeperserver.food.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodCategoryEntity;

import java.util.List;
import java.util.Optional;

public interface FoodCategoryCustomRepository {

    void deleteFoodCategories(String memberKey);

    List<FoodCategoryEntity> findAllByMemberKey(String memberKey);

    Optional<FoodCategoryEntity> findByIdAndMemberKey(Long id, String memberKey);

    List<FoodCategoryEntity> findAllByIdIn(List<Long> ids);
}
