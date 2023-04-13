package com.gentics.graphqlfilter.filter;

import java.util.Optional;
import java.util.function.Predicate;

import com.gentics.graphqlfilter.filter.operation.Combiner;
import com.gentics.graphqlfilter.filter.operation.Comparison;
import com.gentics.graphqlfilter.filter.operation.FilterOperation;
import com.gentics.graphqlfilter.filter.operation.FilterQuery;
import com.gentics.graphqlfilter.filter.operation.UnformalizableQuery;

import graphql.Scalars;
import graphql.schema.GraphQLInputType;

public class BooleanFilter implements Filter<Boolean, Boolean> {

	private static BooleanFilter instance;

	/**
	 * Get the singleton boolean filter
	 */
	public static synchronized BooleanFilter filter() {
		if (instance == null) {
			instance = new BooleanFilter();
		}
		return instance;
	}

	@Override
	public GraphQLInputType getType() {
		return Scalars.GraphQLBoolean;
	}

	@Override
	public Predicate<Boolean> createPredicate(Boolean query) {
		return query::equals;
	}

	@Override
	public FilterOperation<?> createFilterOperation(FilterQuery<?, Boolean> query) throws UnformalizableQuery {
		return Comparison.eq(query.makeFieldOperand(Optional.empty()), query.makeValueOperand(false));
	}
}
