package com.foodkeeper.foodkeeperserver.food.controller.v1.response;

import com.foodkeeper.foodkeeperserver.food.domain.FoodScan;
import com.foodkeeper.foodkeeperserver.food.domain.StorageMethod;

import java.time.LocalDateTime;

public record FoodScanResponse(String name, StorageMethod storageMethod, LocalDateTime expiryDate) {
    public static FoodScanResponse from(FoodScan foodScan) {
        return new FoodScanResponse(
                foodScan.name(),
                foodScan.storageMethod(),
                (foodScan.expiryDate() != null) ? foodScan.expiryDate().atStartOfDay() : null
        );
    }
}
