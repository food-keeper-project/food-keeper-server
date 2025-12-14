package com.foodkeeper.foodkeeperserver.food.domain;

import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record Food(
        Long id,
        String name,
        String imageUrl,
        StorageMethod storageMethod,
        LocalDate expiryDate,
        String memo,
        Integer selectedCategoryCount,
        String memberId
) {
}
