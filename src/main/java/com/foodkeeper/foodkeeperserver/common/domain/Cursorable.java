package com.foodkeeper.foodkeeperserver.common.domain;

public record Cursorable<T>(T cursor, Integer limit) {
    private static final int SYSTEM_MAX = 50;

    public Cursorable(T cursor, Integer limit) {
        this.cursor = cursor;
        if (limit == null || limit <= 0) {
            this.limit = 10;
        } else {
            this.limit = Math.min(limit, SYSTEM_MAX);
        }
    }
}
