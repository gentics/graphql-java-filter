package com.gentics.graphqlfilter.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

public interface FilterUtil {
	static <T> Predicate<T> nullablePredicate(Predicate<T> predicate) {
		return val -> val != null && predicate.test(val);
	}

	@SafeVarargs
	static <U, T extends Collection<U>> T addFluent(T list, U... args) {
		list.addAll(Arrays.asList(args));
		return list;
	}

	static <U, T extends Collection<U>> T addFluent(T list, T arg) {
		list.addAll(arg);
		return list;
	}

	static <U, T extends Collection<U>> T removeFluent(T list, U arg) {
		list.remove(arg);
		return list;
	}

	static <K, V, M extends Map<K, V>> M addFluent(M map, M other) {
		map.putAll(other);
		return map;
	}
}
