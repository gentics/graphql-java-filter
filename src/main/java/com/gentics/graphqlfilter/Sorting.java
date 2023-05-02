package com.gentics.graphqlfilter;

import static graphql.schema.GraphQLEnumType.newEnum;

import com.gentics.graphqlfilter.util.Lazy;

import graphql.schema.GraphQLEnumType;

/**
 * Sorting parameter enumeration.
 * 
 * @author plyhun
 *
 */
public enum Sorting {
	ASCENDING,	DESCENDING;

	public static final String SORT_ORDER_NAME = "SortOrder";

	private static final Lazy<GraphQLEnumType> type = new Lazy<>(() -> newEnum().name(SORT_ORDER_NAME).description("Sort order")
			.value(ASCENDING.name().toUpperCase(), ASCENDING, "Ascending")
			.value(DESCENDING.name().toUpperCase(), DESCENDING, "Descending").build());

	public static final GraphQLEnumType getSortingEnumType() {
		return type.get();
	}
}