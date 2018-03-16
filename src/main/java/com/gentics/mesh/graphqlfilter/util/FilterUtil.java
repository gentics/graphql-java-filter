package com.gentics.mesh.graphqlfilter.util;

import java.util.function.Predicate;

public interface FilterUtil {
    static <T> Predicate<T> nullablePredicate(Predicate<T> predicate) {
        return val -> val != null && predicate.test(val);
    }
}
