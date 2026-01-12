package com.foodkeeper.foodkeeperserver.food.domain;

import java.time.LocalDate;

public record FoodScan(String name, StorageMethod storageMethod, LocalDate expiryDate) {
}
