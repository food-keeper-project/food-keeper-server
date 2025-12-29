package com.foodkeeper.foodkeeperserver.food.domain;

import com.foodkeeper.foodkeeperserver.common.dataaccess.entity.enums.EntityStatus;

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
        EntityStatus status,
        List<String> categoryNames,
        Long remainDays
) {
}
