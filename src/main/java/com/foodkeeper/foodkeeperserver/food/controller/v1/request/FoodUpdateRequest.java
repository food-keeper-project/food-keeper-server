package com.foodkeeper.foodkeeperserver.food.controller.v1.request;

import com.foodkeeper.foodkeeperserver.food.domain.StorageMethod;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodRegister;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record FoodUpdateRequest(
        @NotNull String name,
        @NotNull List<Long> categoryIds,
        @NotNull StorageMethod storageMethod,
        @NotNull LocalDateTime expiryDate,
        @NotNull @Max(14) Integer expiryAlarm,
        @NotNull String memo
) {
    public static FoodRegister toRegister(FoodUpdateRequest request) {
        return new FoodRegister(
                request.name(),
                request.categoryIds,
                request.storageMethod,
                request.expiryDate.toLocalDate(),
                request.expiryAlarm,
                request.memo
        );
    }
}
