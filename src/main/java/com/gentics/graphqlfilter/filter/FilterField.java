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
	 * Creates the field which is used to construct the sorting argument for an GraphQL input type.
	 */
	default GraphQLInputObjectField toSortableObjectField() {
		return newInputObjectField()
			.name(getName())
			.description(getDescription())
			.type(getSortingType())
			.build();
	}

	/**
	 * A filter that tests if a value is null.
	 */
	static <T> FilterField<T, Boolean> isNull() {
		return create("isNull", "Tests if the value is null", GraphQLBoolean, 
				query -> value -> query == (value == null), 
				Optional.of((query) -> query.getQuery() 
						? Comparison.isNull(query.makeFieldOperand(Optional.empty()), query.getInitiatingFilterName()) 
						: Comparison.isNotNull(query.makeFieldOperand(Optional.empty()), query.getInitiatingFilterName()) ));
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
		return create(name, description, type, createPredicate, createFilterDefinition, false);
	}

	/**
	 * A helper method to easily create a FilterField with an optional null check on a value
	 *
	 * @param name
	 *            name of the filter
	 * @param description
	 *            description of the filter
	 * @param type
	 *            GraphQl type of the filter
	 * @param createPredicate
	 *            a function that creates a predicate based on the filter the user defined
	 * @param checkNullValue
	 *            should value be null-checked?
	 * @param <T>
	 *            The predicate input type
	 * @param <Q>
	 *            The Java type mapped from the GraphQL input type
	 */
	static <T, Q> FilterField<T, Q> create(String name, String description, GraphQLInputType type, Function<Q, Predicate<T>> createPredicate, Optional<Function<FilterQuery<?, Q>, FilterOperation<?>>> createFilterDefinition, boolean checkNullValue) {
		return create(name, description, type, createPredicate, createFilterDefinition, checkNullValue, false, Optional.empty(), Optional.empty());
	}

	/**
	 * A helper method to easily create a FilterField with an optional null check on a value, and sortability.
	 * 
	 * @param <T> The predicate input type
	 * @param <Q> The Java type mapped from the GraphQL input type
	 * @param name name of the filter
	 * @param description description of the filter
	 * @param type GraphQl type of the filter
	 * @param createPredicate a function that creates a predicate based on the filter the user defined
	 * @param createFilterDefinition an optional function that creates a native filter definition.
	 * @param checkNullValue should value be null-checked?
	 * @param isSortable is this field sortable?
	 * @param maybeOwner an optional native filter owner value
	 * @param maybeSortingType an optional custom sorting type
	 * @return
	 */
	static <T, Q> FilterField<T, Q> create(String name, String description, GraphQLInputType type, Function<Q, Predicate<T>> createPredicate, 
					Optional<Function<FilterQuery<?, Q>, FilterOperation<?>>> createFilterDefinition, boolean checkNullValue, boolean isSortable,
					Optional<String> maybeOwner, Optional<GraphQLInputType> maybeSortingType) {

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
				Predicate<T> predicate = createPredicate.apply(query);
				return checkNullValue ? ( val -> val == null ? false : predicate.test(val) ) : predicate;
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
					throw new UnformalizableQuery(name, "No operation for this query: " + String.valueOf(query));
				}
			}

			@Override
			public boolean isSortable() {
				return isSortable;
			}

			@Override
			public Optional<String> getOwner() {
				return maybeOwner;
			}

			@Override
			public GraphQLInputType getSortingType() {
				return maybeSortingType.orElseGet(() -> FilterField.super.getSortingType());
			}
		};
	}
}
