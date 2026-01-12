package com.foodkeeper.foodkeeperserver.food.domain;

import java.time.LocalDate;

public record ScannedFood(String name, StorageMethod storageMethod, LocalDate expiryDate) {
}
