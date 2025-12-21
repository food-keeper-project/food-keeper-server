package com.foodkeeper.foodkeeperserver.common.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PageObject<T> {

    private final List<T> contents;

    private final boolean hasNext;

    // 전체 size() 는 항상 limit+1
    public PageObject(List<T> objects, int limit) {
        if (objects.size() > limit) {
            this.hasNext = true;
            this.contents = new ArrayList<>(objects.subList(0, limit));
        } else {
            this.hasNext = false;
            this.contents = objects;
        }
    }

    public List<T> getContent() {
        return Collections.unmodifiableList(contents);
    }

    public boolean hasNext() {
        return hasNext;
    }
}
