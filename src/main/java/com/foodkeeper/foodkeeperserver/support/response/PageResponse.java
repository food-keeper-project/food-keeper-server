package com.foodkeeper.foodkeeperserver.support.response;

import com.foodkeeper.foodkeeperserver.common.domain.SliceObject;

import java.util.List;

public record PageResponse<T>(List<T> content, boolean hasNext) {

    public static <T> PageResponse<T> from(SliceObject<T> slice) {
        return new PageResponse<>(slice.getContent(), slice.isHasNext());
    }
}
