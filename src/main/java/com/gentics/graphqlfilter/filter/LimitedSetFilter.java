package com.gentics.graphqlfilter.filter;

import static graphql.schema.GraphQLEnumType.newEnum;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import com.gentics.graphqlfilter.filter.operation.Comparison;
import com.gentics.graphqlfilter.filter.operation.FilterOperation;
import com.gentics.graphqlfilter.filter.operation.FilterQuery;
import com.gentics.graphqlfilter.filter.operation.UnformalizableQuery;

import graphql.schema.GraphQLEnumType.Builder;
import graphql.schema.GraphQLInputType;

public class LimitedSetFilter<T, Q> implements Filter<T, Q> {

	protected final GraphQLInputType type;
	protected final Function<T, Q> mapper;

	public LimitedSetFilter(String name, T[] values, Function<T, Q> mapper, Function<T, String> nameTheItem, Function<T, String> describeTheItem) {
		Builder builder = newEnum().name(name+ "SetFilter").description("The limited set of values for " + name);		
		Arrays.stream(values).forEach(item -> builder.value(nameTheItem.apply(item), item, describeTheItem.apply(item)));		
		this.type = builder.build();
		this.mapper = mapper;
	}

	@Override
	public GraphQLInputType getType() {
		return type;
	}

	@Override
	public Predicate<T> createPredicate(Q query) {
		return item -> item == null ? false : mapper.apply(item).equals(query);
	}

	@Override
	public FilterOperation<?> createFilterOperation(FilterQuery<?, Q> query) throws UnformalizableQuery {
		return Comparison.eq(query.makeFieldOperand(Optional.empty()), query.makeValueOperand(false), query.getInitiatingFilterName());
	}
}
