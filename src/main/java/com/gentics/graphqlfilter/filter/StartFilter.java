package com.gentics.graphqlfilter.filter;

import graphql.schema.GraphQLArgument;

/**
 * A filter that can be used as a beginning of a nested GraphQL filter. For that it needs to be able to create a GraphQL filter argument.
 *
 * Implement this interface on any filter to be able to use it as a start filter.
 */
public interface StartFilter<T, Q> extends Filter<T, Q> {
	/**
	 * Creates the filter argument for GraphQL
	 */
	default GraphQLArgument createFilterArgument() {
		return GraphQLArgument.newArgument()
			.name("filter")
			.description("Filters the list of elements")
			.type(getType())
			.build();
	}

	default GraphQLArgument createSortArgument() {
		return GraphQLArgument.newArgument()
				.name("sort")
				.description("Sorts the list of elements")
				.type(getSortingType())
				.build();
	}
}
