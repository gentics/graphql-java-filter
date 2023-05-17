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

public class EnumFilter<T extends Enum<?>> implements Filter<T, T> {

	private static final Map<Class<? extends Enum<?>>, EnumFilter<?>> instances = new HashMap<>();

	private final GraphQLInputType type;

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
	}

	@Override
	public GraphQLInputType getType() {
		return type;
	}

	@Override
	public Predicate<T> createPredicate(T query) {
		return query::equals;
	}

	@Override
	public FilterOperation<?> createFilterOperation(FilterQuery<?, T> query) throws UnformalizableQuery {
		return Comparison.eq(query.makeFieldOperand(Optional.empty()), query.makeValueOperand(false));
	}
}
