package com.foodkeeper.foodkeeperserver.support.ratelimit;

public record LimitApi(String method, String url) {
    public static LimitApi pattern(String method, String url) {
        return new LimitApi(method, url);
    }

    public static LimitApi pattern(String url) {
        return new LimitApi(null, url);
    }

    public boolean noMethod() {
        return this.method == null;
    }
}
