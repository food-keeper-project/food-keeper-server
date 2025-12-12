package com.foodkeeper.foodkeeperserver.domain.food.dto.request;

import com.foodkeeper.foodkeeperserver.domain.food.entity.Food;
import com.foodkeeper.foodkeeperserver.domain.food.entity.FoodCategory;
import com.foodkeeper.foodkeeperserver.domain.food.entity.StorageMethod;

import java.time.LocalDate;
import java.util.List;

public record FoodRegisterRequest(String name, List<FoodCategory> foodCategories, StorageMethod storageMethod, LocalDate expiryDate, Integer expiryAlertDaysBefore, String memo) {
    public static Food toEntity(FoodRegisterRequest request,String url,String memberId){
        return Food.builder()
                .name(request.name())
                .imageUrl(url)
                .storageMethod(request.storageMethod)
                .expiryDate(request.expiryDate)
                .expiryAlertDaysBefore(request.expiryAlertDaysBefore)
                .memo(request.memo())
                .memberId(memberId)
                .build();
    }
}
