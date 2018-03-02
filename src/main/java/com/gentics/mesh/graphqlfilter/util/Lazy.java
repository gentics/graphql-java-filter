package com.gentics.mesh.graphqlfilter.util;

import java.util.Objects;
import java.util.function.Supplier;

public class Lazy<T> implements Supplier<T> {
    private T value;
    private final Supplier<T> supplier;

    public Lazy(Supplier<T> supplier) {
        Objects.requireNonNull(supplier);
        this.supplier = supplier;
    }

    @Override
    public T get() {
        if (value == null) {
            value = supplier.get();
        }
        return value;
    }
}
