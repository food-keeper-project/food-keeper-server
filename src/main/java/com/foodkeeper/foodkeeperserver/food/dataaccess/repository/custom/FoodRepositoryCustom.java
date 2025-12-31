package com.foodkeeper.foodkeeperserver.food.dataaccess.repository.custom;

import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.foodkeeper.foodkeeperserver.common.domain.SliceObject;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface FoodRepositoryCustom {
    SliceObject<FoodEntity> findFoodCursorList(Cursorable<Long> cursorable, Long categoryId, String memberKey);

    List<FoodEntity> findAllByMemberKey(String memberKey);

    List<FoodEntity> findImminentFoods(String memberKey);

    List<FoodEntity> findFoodsToNotify(LocalDate today);

}
