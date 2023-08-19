package com.codekoi.util;

import java.util.List;
import java.util.function.Function;

public class ListExtractor {

    public static <T, K> List<K> parse(List<T> list, Function<T, K> getter) {
        return list.stream()
                .map(getter)
                .toList();
    }
}
