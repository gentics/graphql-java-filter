package com.gentics.graphqlfilter.filter;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import com.gentics.graphqlfilter.filter.sql.SqlField;
import com.gentics.graphqlfilter.filter.sql.SqlPredicate;
import com.gentics.graphqlfilter.filter.sql2.FilterQuery;
import com.gentics.graphqlfilter.filter.sql2.FilterOperation;

import graphql.schema.GraphQLInputType;

/**
 * The implementation of a filter represented as a GraphQL input type.
 *
 * The idea is to create a predicate based on the filter a user is providing in a query, which then can be used to filter the collection of items the user is
 * requesting.
 *
 * @param <T>
 *            The type of items that will be filtered
 * @param <Q>
 *            The Java representation of the GraphQLInputType of this query.
 */
public interface Filter<T, Q> {

	/**
	 * The type of this filter to be used in GraphQl. This describes the options the user can choose to refine the filter.
	 */
	GraphQLInputType getType();

	/**
	 * Creates a predicate based on the filter the user provided
	 * 
	 * @param query
	 *            The Java representation of the user defined query.
	 * @return a predicate to filter elements
	 */
	Predicate<T> createPredicate(Q query);

	Optional<String> getOwner();

	default Optional<SqlPredicate> maybeGetSqlDefinition(Q query, List<SqlField<?>> field) {
		return Optional.empty();
	}

	default Optional<FilterOperation<?>> maybeGetFilterOperation(FilterQuery<?, Q> query) {
		return Optional.empty();
	}
}
