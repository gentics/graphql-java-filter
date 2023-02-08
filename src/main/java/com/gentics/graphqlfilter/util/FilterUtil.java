package com.gentics.graphqlfilter.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public interface FilterUtil {
	static <T> Predicate<T> nullablePredicate(Predicate<T> predicate) {
		return val -> val != null && predicate.test(val);
	}

	@SafeVarargs
	static <U, T extends List<U>> T addFluent(T list, U... args) {
		list.addAll(Arrays.asList(args));
		return list;
	}

	static <U, T extends List<U>> T removeFluent(T list, U arg) {
		list.remove(arg);
		return list;
	}

	static <K, V, M extends Map<K, V>> M addFluent(M map, M other) {
		map.putAll(other);
		return map;
	}
}
