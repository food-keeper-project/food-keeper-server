package com.foodkeeper.foodkeeperserver.food.controller.v1.request;

import com.foodkeeper.foodkeeperserver.food.domain.StorageMethod;
import com.foodkeeper.foodkeeperserver.food.domain.request.FoodRegister;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

//todo 카테고리 선택수 검증
public record FoodRegisterRequest(
        @NotBlank String name,
        @NotNull @Size(min = 1, max = 3) List<Long> categoryIds,
        @NotNull StorageMethod storageMethod,
        @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expiryDate,
        @NotNull @Size(min = 0, max = 14) Integer expiryAlarm,
        @NotNull String memo
) {
    public static FoodRegister toRegister(FoodRegisterRequest request) {
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
