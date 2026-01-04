package com.foodkeeper.foodkeeperserver.food.domain.request;

import com.foodkeeper.foodkeeperserver.food.domain.StorageMethod;

import java.time.LocalDate;
import java.util.List;

public record FoodRegister(String name,
                           List<Long> categoryIds,
                           StorageMethod storageMethod,
                           LocalDate expiryDate,
                           Integer expiryAlarmDays,
                           String memo) {
}
