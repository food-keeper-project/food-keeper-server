package com.foodkeeper.foodkeeperserver.food.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface FoodRepositoryCustom {
    List<FoodEntity> findFoodCursorList(Cursorable cursorable, Long categoryId, LocalDateTime lastCreatedAt, String memberKey);

    List<FoodEntity> findAllByMemberKey(String memberKey);

    List<FoodEntity> findImminentFoods(String memberKey);

}
