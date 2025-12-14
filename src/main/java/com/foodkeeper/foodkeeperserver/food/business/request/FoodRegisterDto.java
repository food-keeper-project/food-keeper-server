package com.foodkeeper.foodkeeperserver.food.business.request;

import com.foodkeeper.foodkeeperserver.food.controller.v1.request.FoodRegisterRequest;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.domain.StorageMethod;

import java.time.LocalDate;
import java.util.List;

public record FoodRegisterDto(String name,
                              List<Long> categoryIds,
                              StorageMethod storageMethod,
                              LocalDate expiryDate,
                              String memo) {

    public Food toDomain(String imageUrl, String memberId) {
        return Food.builder()
                .name(this.name)
                .imageUrl(imageUrl)
                .storageMethod(this.storageMethod)
                .expiryDate(this.expiryDate)
                .memo(this.memo)
                .selectedCategoryCount(this.categoryIds.size())
                .memberId(memberId)
                .build();
    }
}
