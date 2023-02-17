package com.gentics.graphqlfilter.filter;

import java.util.Optional;
import java.util.function.Predicate;

import com.gentics.graphqlfilter.Sorting;
import com.gentics.graphqlfilter.filter.operation.FilterOperation;
import com.gentics.graphqlfilter.filter.operation.FilterQuery;
import com.gentics.graphqlfilter.filter.operation.UnformalizableQuery;

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
	 * The sorting type of this filter. If this filter contains fields, the original type is returned. Otherwise the terminating {@link Sorting} enumeration is considered.
	 * 
	 * @return
	 */
	default GraphQLInputType getSortingType() {
		return isSortable() ? Sorting.getSortingEnumType() : getType();
	}

	/**
	 * Creates a predicate based on the filter the user provided
	 * 
	 * @param query
	 *            The Java representation of the user defined query.
	 * @return a predicate to filter elements
	 */
	Predicate<T> createPredicate(Q query);

	/**
	 * Is this filter supports sorting over input values?
	 * 
	 * @return
	 */
	default boolean isSortable() {
		return true;
	}

	/**
	 * Get the name of an entity owning this filter, if available. If not, a filter owner is considered the one level above. 
	 * The owner data is required to build a formalized filter representation. For instance, the entity name or field name has a strict owner, either a entity type or an entity,
	 * but the distinct operations like `equals` or `greater-than` have no owner, because may be called from anywhere.
	 * 
	 * @return
	 */
	default Optional<String> getOwner() {
		return Optional.empty();
	}

	/**
	 * Create the formal representation of a filter. Not every filter is possible to formalize into the supported operation, 
	 * so by default this method throws an error.
	 * 
	 * @param query
	 * @return
	 * @throws UnformalizableQuery
	 */
	default FilterOperation<?> createFilterOperation(FilterQuery<?, Q> query) throws UnformalizableQuery {
		throw new UnformalizableQuery("No operation for this query: " + String.valueOf(query));
	}
}
