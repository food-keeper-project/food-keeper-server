package com.foodkeeper.foodkeeperserver.food.domain;

import lombok.Getter;

@Getter
public enum DefaultFoodCategory {
    VEGETABLES("야채류"),
    MEAT("육류"),
    SEAFOOD("해산물"),
    DAIRY_PRODUCTS("유제품"),
    FRUIT("과일류"),
    ETC("기타"),
    ;

    private final String value;

    DefaultFoodCategory(String value) {
        this.value = value;
    }
}
