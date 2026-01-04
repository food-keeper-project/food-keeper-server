package com.foodkeeper.foodkeeperserver.bookmarkedfood.domain;

import com.foodkeeper.foodkeeperserver.food.domain.StorageMethod;

public record BookmarkedFood(String name, String imageUrl, StorageMethod storageMethod) {
}
