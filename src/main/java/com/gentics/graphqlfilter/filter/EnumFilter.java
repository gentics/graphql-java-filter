package com.gentics.graphqlfilter.filter;

import static graphql.schema.GraphQLEnumType.newEnum;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import com.gentics.graphqlfilter.filter.operation.Comparison;
import com.gentics.graphqlfilter.filter.operation.FilterOperation;
import com.gentics.graphqlfilter.filter.operation.FilterQuery;
import com.gentics.graphqlfilter.filter.operation.UnformalizableQuery;

import graphql.schema.GraphQLEnumType.Builder;
import graphql.schema.GraphQLInputType;

public class EnumFilter<T extends Enum<?>> implements Filter<T, String> {

	private static final Map<Class<? extends Enum<?>>, EnumFilter<?>> instances = new HashMap<>();

	private final GraphQLInputType type;
	private final Class<T> enumClass;

	/**
	 * Get the singleton filter
	 */
	@SuppressWarnings("unchecked")
	public static synchronized <T extends Enum<?>> EnumFilter<T> filter(Class<T> enumClass) {
		return (EnumFilter<T>) instances.computeIfAbsent(enumClass, cls -> new EnumFilter<>(cls));
	}

	private EnumFilter(Class<T> enumClass) {
		Builder builder = newEnum().name(enumClass.getSimpleName() + "Filter").description("The limited set of values for " + enumClass.getSimpleName());
		
		Arrays.stream(enumClass.getEnumConstants()).forEach(item -> builder.value(item.name(), item, item.toString()));
		
		this.type = builder.build();
		this.enumClass = enumClass;
	}

	@Override
	public GraphQLInputType getType() {
		return type;
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "static-access" })
	@Override
	public Predicate<T> createPredicate(String query) {
		Class untypedClass = enumClass;
		return val -> val == null ? false : enumClass.getEnumConstants()[0].valueOf(untypedClass, query) == val;
	}

	@Override
	public FilterOperation<?> createFilterOperation(FilterQuery<?, String> query) throws UnformalizableQuery {
		return Comparison.eq(query.makeFieldOperand(Optional.empty()), query.makeValueOperand(false));
	}
}
