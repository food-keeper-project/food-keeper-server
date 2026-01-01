package com.foodkeeper.foodkeeperserver.food.controller.v1.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.foodkeeper.foodkeeperserver.food.domain.StorageMethod;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodRegister;
import jakarta.validation.constraints.Max;

import java.time.LocalDate;
import java.util.List;

public record FoodUpdateRequest(
        String name,
        List<Long> categoryIds,
        StorageMethod storageMethod,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate expiryDate,
        @Max(14) Integer expiryAlarm,
        String memo
) {
    public static FoodRegister toRegister(FoodUpdateRequest request) {
        return new FoodRegister(
                request.name(),
                request.categoryIds,
                request.storageMethod,
                request.expiryDate,
                request.expiryAlarm,
                request.memo
        );
    }
}
