package com.foodkeeper.foodkeeperserver.food.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum StorageMethod {
    REFRIGERATED("냉장"),
    FROZEN("냉동"),
    ROOM_TEMP("실온");

    private final String description;

    @JsonValue
    public String getValue() {
        return description;
    }

}
