package com.foodkeeper.foodkeeperserver.common.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
@Getter
public class SliceObject<T> {
    private final List<T> content;
    private final Cursorable cursorable;
    private final boolean hasNext;

    public SliceObject(List<T> content, Cursorable cursorable, boolean hasNext) {
        this.content = new ArrayList<>(content);
        this.cursorable = cursorable;
        this.hasNext = hasNext;
    }

    public SliceObject(List<T> content, Cursorable cursorable) {
        this.content = new ArrayList<>(content);
        this.cursorable = cursorable;

        boolean hasNext = false;
        if (this.content.size() > cursorable.getLimit()) {
            hasNext = true;
            this.content.removeLast();
        }
        this.hasNext = hasNext;
    }

}
