package com.codekoi.pagination;

import lombok.Getter;

import java.util.List;
import java.util.function.Function;

@Getter
public class NoOffSetPagination<T, K> {

    private List<T> list;
    private boolean hasNext = false;
    private K nextId;

    public NoOffSetPagination(List<T> list, boolean hasNext, K nextId) {
        this.list = list;
        this.hasNext = hasNext;
        this.nextId = nextId;
    }

    public NoOffSetPagination(List<T> list, int pageSize, Function<T, K> idGetter) {
        if (list.size() > pageSize) {
            final T nextElement = list.get(list.size() - 1);
            this.nextId = idGetter.apply(nextElement);

            list.remove(nextElement);
            this.hasNext = true;
        }
        this.list = list;
    }
}