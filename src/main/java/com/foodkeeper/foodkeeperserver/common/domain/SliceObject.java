package com.foodkeeper.foodkeeperserver.common.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public record SliceObject<T>(List<T> content, Cursorable<?> cursorable, boolean hasNext) {

    public SliceObject(List<T> content, Cursorable<?> cursorable, boolean hasNext) {
        this.content = new ArrayList<>(content);
        this.cursorable = cursorable;
        this.hasNext = hasNext;
    }

    public <U> SliceObject<U> map(Function<T, U> converter) {
        List<U> mapped = this.content.stream()
                .map(converter)
                .toList();
        return new SliceObject<>(mapped, this.cursorable, this.hasNext);
    }

}
