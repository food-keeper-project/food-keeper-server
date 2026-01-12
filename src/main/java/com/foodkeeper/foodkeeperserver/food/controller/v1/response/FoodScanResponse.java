package com.foodkeeper.foodkeeperserver.food.controller.v1.response;

import com.foodkeeper.foodkeeperserver.food.domain.ScannedFood;
import com.foodkeeper.foodkeeperserver.food.domain.StorageMethod;

import java.time.LocalDateTime;

public record FoodScanResponse(String name, StorageMethod storageMethod, LocalDateTime expiryDate) {
    public static FoodScanResponse from(ScannedFood scannedFood) {
        return new FoodScanResponse(
                scannedFood.name(),
                scannedFood.storageMethod(),
                (scannedFood.expiryDate() != null) ? scannedFood.expiryDate().atStartOfDay() : null
        );
    }
}
