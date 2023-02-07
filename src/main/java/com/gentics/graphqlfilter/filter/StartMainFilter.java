package com.gentics.graphqlfilter.filter;

import java.util.Map;
import java.util.Optional;

/**
 * A start filter that is also a main filter.
 */
public abstract class StartMainFilter<T> extends MainFilter<T> implements StartFilter<T, Map<String, ?>> {
	public StartMainFilter(String name, String description, Optional<String> ownerType) {
		super(name, description, true, ownerType);
	}
}
