package com.gentics.graphqlfilter.filter;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Predicate;

import com.gentics.graphqlfilter.filter.sql.AndPredicate;
import com.gentics.graphqlfilter.filter.sql.CombinerPredicate;
import com.gentics.graphqlfilter.filter.sql.ComparisonPredicate;
import com.gentics.graphqlfilter.filter.sql.NotPredicate;
import com.gentics.graphqlfilter.filter.sql.OrPredicate;
import com.gentics.graphqlfilter.filter.sql.SqlField;
import com.gentics.graphqlfilter.filter.sql.SqlPredicate;

import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLTypeReference;

/**
 * Creates common filters for a type. This is used in {@link MainFilter} to combine multiple filters.
 */
public class CommonFilters {

	/**
	 * Creates the following filters for the provided type:
	 * <ul>
	 * <li>and</li>
	 * <li>or</li>
	 * <li>not</li>
	 * </ul>
	 *
	 * @param filter
	 *            The filter to create types for
	 * @param type
	 *            The reference type of the filter. We can't use the actual type since this would lead to an endless recursion.
	 * @param <T>
	 *            The input type of the predicate
	 */
	public static <T> List<FilterField<T, ?>> createFor(Filter<T, ?> filter, GraphQLTypeReference type) {
		return Arrays.asList(
			orFilter(filter, type),
			andFilter(filter, type),
			notFilter(filter, type));
	}

	private static <T, Q> FilterField<T, List<Q>> orFilter(Filter<T, Q> filter, GraphQLInputType type) {
		return new FilterField<T, List<Q>>() {
			@Override
			public String getName() {
				return "or";
			}

			@Override
			public String getDescription() {
				return "Applies if any filters match.";
			}

			@Override
			public Predicate<T> createPredicate(List<Q> query) {
				return query.stream()
					.map(filter::createPredicate)
					.reduce(Predicate::or)
					.orElse(ignore -> true);
			}

			@Override
			public GraphQLInputType getType() {
				return GraphQLList.list(type);
			}

			@Override
			public Optional<SqlPredicate> maybeGetSqlDefinition(List<Q> query, List<SqlField<?>> fields) {
				try {
					return Optional.of(query.stream()
						.map(entry -> filter.maybeGetSqlDefinition(entry, fields))
						.map(p -> p.orElseThrow(() -> new NoSuchElementException()))
						.reduce((CombinerPredicate) new OrPredicate(), (and, p1) -> and.addPredicate(p1), (p1, p2) -> p1));
				} catch (NoSuchElementException e) {
					return Optional.empty();
				}
			}

			@Override
			public Optional<String> getOwner() {
				return Optional.empty();
			}
		};
	}

	private static <T, Q> FilterField<T, List<Q>> andFilter(Filter<T, Q> filter, GraphQLInputType type) {
		return new FilterField<T, List<Q>>() {
			@Override
			public String getName() {
				return "and";
			}

			@Override
			public String getDescription() {
				return "Applies if all filters match.";
			}

			@Override
			public Predicate<T> createPredicate(List<Q> query) {
				return query.stream()
					.map(filter::createPredicate)
					.reduce(Predicate::and)
					.orElse(ignore -> true);
			}

			@Override
			public GraphQLInputType getType() {
				return GraphQLList.list(type);
			}

			@Override
			public Optional<SqlPredicate> maybeGetSqlDefinition(List<Q> query, List<SqlField<?>> fields) {
				try {
					return Optional.of(query.stream()
						.map(entry -> filter.maybeGetSqlDefinition(entry, fields))
						.map(p -> p.orElseThrow(() -> new NoSuchElementException()))
						.reduce((CombinerPredicate) new AndPredicate(), (and, p1) -> and.addPredicate(p1), (p1, p2) -> p1));
				} catch (NoSuchElementException e) {
					return Optional.empty();
				}
			}

			@Override
			public Optional<String> getOwner() {
				return Optional.empty();
			}
		};
	}

	private static <T, Q> FilterField<T, Q> notFilter(Filter<T, Q> filter, GraphQLInputType type) {
		return new FilterField<T, Q>() {
			@Override
			public String getName() {
				return "not";
			}

			@Override
			public String getDescription() {
				return "Negates a filter.";
			}

			@Override
			public Predicate<T> createPredicate(Q query) {
				return filter.createPredicate(query).negate();
			}

			@Override
			public GraphQLInputType getType() {
				return type;
			}

			@SuppressWarnings("unchecked")
			@Override
			public Optional<SqlPredicate> maybeGetSqlDefinition(Q query, List<SqlField<?>> fields) {
				return filter.maybeGetSqlDefinition(query, fields).filter(ComparisonPredicate.class::isInstance).map(p -> new NotPredicate<>((ComparisonPredicate<Q>) p));
			}

			@Override
			public Optional<String> getOwner() {
				return Optional.empty();
			}
		};
	}
}
