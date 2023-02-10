package com.gentics.graphqlfilter.filter;

import static graphql.Scalars.GraphQLBoolean;
import static graphql.schema.GraphQLInputObjectField.newInputObjectField;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import com.gentics.graphqlfilter.filter.operation.Comparison;
import com.gentics.graphqlfilter.filter.operation.FilterOperation;
import com.gentics.graphqlfilter.filter.operation.FilterQuery;
import com.gentics.graphqlfilter.filter.operation.UnformalizableQuery;

import graphql.schema.GraphQLInputObjectField;
import graphql.schema.GraphQLInputType;

/**
 * A filter that can be used inside other nested filters, such as the {@link MainFilter}
 */
public interface FilterField<T, Q> extends Filter<T, Q> {
	/**
	 * The name of the field in the GraphQLInputObject
	 */
	String getName();

	/**
	 * The description of the field in the GraphQLInputObject
	 */
	String getDescription();

	/**
	 * Creates the field which is used to construct the GraphQL input type.
	 */
	default GraphQLInputObjectField toObjectField() {
		return newInputObjectField()
			.name(getName())
			.description(getDescription())
			.type(getType())
			.build();
	}

	/**
	 * A filter that tests if a value is null.
	 */
	static <T> FilterField<T, Boolean> isNull() {
		return create("isNull", "Tests if the value is null", GraphQLBoolean, 
				query -> value -> query == (value == null), 
				Optional.of((query) -> Comparison.isNull(query.makeFieldOperand(Optional.empty()))));
	}

	/**
	 * A helper method to easily create a FilterField.
	 *
	 * @param name
	 *            name of the filter
	 * @param description
	 *            description of the filter
	 * @param type
	 *            GraphQl type of the filter
	 * @param createPredicate
	 *            a function that creates a predicate based on the filter the user defined
	 * @param <T>
	 *            The predicate input type
	 * @param <Q>
	 *            The Java type mapped from the GraphQL input type
	 */
	static <T, Q> FilterField<T, Q> create(String name, String description, GraphQLInputType type, Function<Q, Predicate<T>> createPredicate, Optional<Function<FilterQuery<?, Q>, FilterOperation<?>>> createFilterDefinition) {
		return new FilterField<T, Q>() {
			@Override
			public String getName() {
				return name;
			}

			@Override
			public String getDescription() {
				return description;
			}

			@Override
			public Predicate<T> createPredicate(Q query) {
				return createPredicate.apply(query);
			}

			@Override
			public GraphQLInputType getType() {
				return type;
			}

			@Override
			public FilterOperation<?> createFilterOperation(FilterQuery<?, Q> query) throws UnformalizableQuery {
				if (createFilterDefinition.isPresent()) {
					return createFilterDefinition.map(f -> f.apply(query)).get();
				} else {
					throw new UnformalizableQuery("No operation for this query: " + String.valueOf(query));
				}
			}

		};
	}
}
