package com.foodkeeper.foodkeeperserver.common.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Getter
public class SliceObject<T> {
    private final List<T> content;
    private final Cursorable<?> cursorable;
    private final boolean hasNext;

    public SliceObject(List<T> content, Cursorable<?> cursorable, boolean hasNext) {
        this.content = new ArrayList<>(content);
        this.cursorable = cursorable;
        this.hasNext = hasNext;
    }

    public SliceObject(List<T> content, Cursorable<?> cursorable) {
        this.content = new ArrayList<>(content);
        this.cursorable = cursorable;

        boolean hasNext = false;
        if (this.content.size() > cursorable.limit()) {
            hasNext = true;
            this.content.removeLast();
        }
        this.hasNext = hasNext;
    }

    public <U> SliceObject<U> map(Function<T, U> converter) {
        List<U> mapped = this.content.stream()
                .map(converter)
                .toList();
        return new SliceObject<>(mapped, this.cursorable, this.hasNext);
    }

}
