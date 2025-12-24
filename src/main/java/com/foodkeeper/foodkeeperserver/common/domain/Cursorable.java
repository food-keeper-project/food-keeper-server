package com.foodkeeper.foodkeeperserver.common.domain;

import lombok.Getter;

@Getter
public final class Cursorable {
    private static final int SYSTEM_MAX = 50;

    private final Long cursor;
    private final Integer limit;

    public Cursorable(Long cursor, Integer limit) {
        this.cursor = cursor;
        if (limit == null || limit <= 0) {
            this.limit = 10;
        } else {
            this.limit = Math.min(limit, SYSTEM_MAX);
        }
    }
}
