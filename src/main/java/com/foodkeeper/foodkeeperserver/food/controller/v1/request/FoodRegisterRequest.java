package com.foodkeeper.foodkeeperserver.food.controller.v1.request;

import com.foodkeeper.foodkeeperserver.food.business.request.FoodRegisterDto;
import com.foodkeeper.foodkeeperserver.food.dataaccess.entity.FoodEntity;
import com.foodkeeper.foodkeeperserver.food.domain.StorageMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

//todo 카테고리 선택수 검증
public record FoodRegisterRequest(
        @NotBlank String name,
        @NotNull @Size(min = 1, max = 3) List<Long> categoryIds,
        @NotNull StorageMethod storageMethod,
        LocalDate expiryDate,
        @NotNull String memo
) {
    public static FoodRegisterDto toDto(FoodRegisterRequest request){
        return new FoodRegisterDto(
                request.name(),
                request.categoryIds,
                request.storageMethod,
                request.expiryDate,
                request.memo
        );
    }
}
