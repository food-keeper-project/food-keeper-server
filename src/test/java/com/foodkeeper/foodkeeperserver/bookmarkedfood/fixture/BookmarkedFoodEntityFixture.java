package com.foodkeeper.foodkeeperserver.bookmarkedfood.fixture;

import com.foodkeeper.foodkeeperserver.bookmarkedfood.dataaccess.entity.BookmarkedFoodEntity;
import com.foodkeeper.foodkeeperserver.food.domain.StorageMethod;

public enum BookmarkedFoodEntityFixture {
    DEFAULT("name", "https://test.com/image.jpg", StorageMethod.FROZEN)
    ;

    private final String name;
    private final String imageUrl;
    private final StorageMethod storageMethod;

    BookmarkedFoodEntityFixture(String name, String imageUrl, StorageMethod storageMethod) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.storageMethod = storageMethod;
    }

    public BookmarkedFoodEntity get(String memberKey) {
        return BookmarkedFoodEntity.builder()
                .name(name)
                .imageUrl(imageUrl)
                .storageMethod(storageMethod)
                .memberKey(memberKey)
                .build();
    }
}
