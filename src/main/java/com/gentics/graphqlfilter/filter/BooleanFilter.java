package com.gentics.graphqlfilter.filter;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import com.gentics.graphqlfilter.filter.sql.ComparisonPredicate;
import com.gentics.graphqlfilter.filter.sql.SqlField;
import com.gentics.graphqlfilter.filter.sql.SqlPredicate;

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
	public Optional<SqlPredicate> maybeGetSqlDefinition(Boolean query, List<SqlField<?>> fields) {
		return Optional.of(new ComparisonPredicate<>("=", fields, query, false));
	}

	@Override
	public Optional<String> getOwner() {
		return Optional.empty();
	}
}
