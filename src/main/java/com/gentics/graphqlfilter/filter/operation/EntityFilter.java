package com.gentics.graphqlfilter.filter.operation;

import java.util.Map;
import java.util.function.Function;

public interface EntityFilter {

	Map<String, Function<Object, FilterOperation<?>>> getFilterOperations();

	static Map.Entry<String, Function<Object, FilterOperation<?>>> makeFilter(String k, Function<Object, FilterOperation<?>> v) {
		return new Map.Entry<String, Function<Object, FilterOperation<?>>>() {
			@Override
			public String getKey() { return k; }

			@Override
			public Function<Object, FilterOperation<?>> getValue() { return v; }

			@Override
			public Function<Object, FilterOperation<?>> setValue(Function<Object, FilterOperation<?>> value) { return value; }
		};
	} 
}
