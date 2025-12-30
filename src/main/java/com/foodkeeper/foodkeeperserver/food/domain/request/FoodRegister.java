package com.foodkeeper.foodkeeperserver.food.domain.request;

import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.domain.StorageMethod;

import java.time.LocalDate;
import java.util.List;

public record FoodRegister(String name,
                           List<Long> categoryIds,
                           StorageMethod storageMethod,
                           LocalDate expiryDate,
                           Integer expiryAlarm,
                           String memo) {

    public Food toNewFood(String imageUrl, String memberKey) {
        return Food.builder()
                .name(this.name)
                .imageUrl(imageUrl)
                .storageMethod(this.storageMethod)
                .expiryDate(this.expiryDate)
                .expiryAlarm(this.expiryAlarm)
                .memo(this.memo)
                .selectedCategoryCount(this.categoryIds.size())
                .memberKey(memberKey)
                .build();
    }

}
