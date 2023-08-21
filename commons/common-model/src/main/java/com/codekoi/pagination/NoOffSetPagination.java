package com.codekoi.pagination;

import lombok.Getter;

import java.util.List;
import java.util.function.Function;

@Getter
public class NoOffSetPagination<T, K> {

    private List<T> list;
    private boolean hasNext = false;
    private K lastId;

    public NoOffSetPagination(List<T> list, boolean hasNext, K lastId) {
        this.list = list;
        this.hasNext = hasNext;
        this.lastId = lastId;
    }

    public NoOffSetPagination(List<T> list, int pageSize, Function<T, K> idGetter) {
        if (list.size() > pageSize) {
            list.remove(pageSize);
            this.hasNext = true;
        }
        this.list = list;

        final T lastElement = list.get(list.size() - 1);
        this.lastId = idGetter.apply(lastElement);
    }
}