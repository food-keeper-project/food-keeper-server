package com.foodkeeper.foodkeeperserver.food.domain;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record Food(
        Long id,
        String name,
        String imageUrl,
        StorageMethod storageMethod,
        LocalDate expiryDate,
        String memo,
        Integer selectedCategoryCount,
        String memberId,
        LocalDateTime createdAt
) {
}
