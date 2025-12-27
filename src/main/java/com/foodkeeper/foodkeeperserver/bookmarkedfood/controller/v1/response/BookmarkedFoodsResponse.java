package com.foodkeeper.foodkeeperserver.bookmarkedfood.controller.v1.response;

import com.foodkeeper.foodkeeperserver.bookmarkedfood.domain.BookmarkedFood;
import com.foodkeeper.foodkeeperserver.food.domain.StorageMethod;

public record BookmarkedFoodsResponse(String name, String imageUrl, StorageMethod storageMethod) {

    public static BookmarkedFoodsResponse from(BookmarkedFood bookmarkedFood) {
        return new BookmarkedFoodsResponse(
                bookmarkedFood.name(),
                bookmarkedFood.imageUrl(),
                bookmarkedFood.storageMethod()
        );
    }
}
