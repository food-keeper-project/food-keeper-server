package com.foodkeeper.foodkeeperserver.food.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record RegisteredFood(
        Long id,
        String name,
        String imageUrl,
        StorageMethod storageMethod,
        LocalDate expiryDate,
        Integer expiryAlarm,
        String memo,
        LocalDateTime createdAt,
        List<Long> categoryIds
) {
}
