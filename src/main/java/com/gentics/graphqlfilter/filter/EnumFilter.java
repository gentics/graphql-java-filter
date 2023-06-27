package com.gentics.graphqlfilter.filter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Filter over limited set of precompiled values (aka Enum).
 * 
 * @author plyhun
 *
 * @param <T>
 */
public class EnumFilter<T extends Enum<?>> extends LimitedSetFilter<T, T> {

	private static final Map<Class<? extends Enum<?>>, EnumFilter<?>> instances = new HashMap<>();


	/**
	 * Get the singleton filter
	 */
	@SuppressWarnings("unchecked")
	public static synchronized <T extends Enum<?>> EnumFilter<T> filter(Class<T> enumClass) {
		return (EnumFilter<T>) instances.computeIfAbsent(enumClass, cls -> new EnumFilter<>(cls));
	}

	private EnumFilter(Class<T> enumClass) {
		super(enumClass.getSimpleName(), enumClass.getEnumConstants(), Function.identity(), Enum::name, Object::toString);
	}
}
