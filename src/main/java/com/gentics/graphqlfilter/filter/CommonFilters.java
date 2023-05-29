package com.gentics.graphqlfilter.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import com.gentics.graphqlfilter.filter.operation.Combiner;
import com.gentics.graphqlfilter.filter.operation.FilterOperation;
import com.gentics.graphqlfilter.filter.operation.FilterQuery;
import com.gentics.graphqlfilter.filter.operation.UnformalizableQuery;

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
			public boolean isSortable() {
				return false;
			}

			@Override
			public FilterOperation<?> createFilterOperation(FilterQuery<?, List<Q>> query) throws UnformalizableQuery {
				return createCombinedFilterOperation(filter, query, list -> {
					// TODO FIXME GraphQL syntax presents wrapping filters as key-value map, which is not always obviously parsed as multi-item list,
					// but rather as a single-operand inner filter, with a default AND operation. The hack below is to process the wrapping OR op correctly.
					// The other common combiners tolerate default AND, and so do not require this fix.
					if (list.size() == 1 && list.get(0).maybeCombination().isPresent()) {
						return Combiner.or(list.get(0).maybeCombination().get(), query.getInitiatingFilterName());
					} else {
						return Combiner.or(list, query.getInitiatingFilterName());
					}
				});
			}
		};
	}

	private static <O, Q> FilterOperation<?> createCombinedFilterOperation(Filter<O, Q> filter, FilterQuery<?, List<Q>> query, Function<List<FilterOperation<?>>, FilterOperation<?>> combiner) throws UnformalizableQuery {
		List<FilterOperation<?>> args = new ArrayList<>();
		for (Q q: query.getQuery()) {
			args.add(filter.createFilterOperation(new FilterQuery<>(
				filter.getOwner().orElse(String.valueOf(query.getOwner())), 
				(filter instanceof NamedFilter) ? ((NamedFilter<?,?>) filter).getName() : "",
				query.getField(), 
				q, 
				query.maybeGetJoins())).maybeSetFilterId(filter.maybeGetFilterId()));
		}
		return combiner.apply(args);
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
			public boolean isSortable() {
				return false;
			}

			@Override
			public FilterOperation<?> createFilterOperation(FilterQuery<?, List<Q>> query) throws UnformalizableQuery {
				return createCombinedFilterOperation(filter, query, list -> Combiner.and(list, query.getInitiatingFilterName()));
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

			@Override
			public boolean isSortable() {
				return false;
			}

			@Override
			public FilterOperation<?> createFilterOperation(FilterQuery<?, Q> query) throws UnformalizableQuery {
				return Combiner.not(filter.createFilterOperation(new FilterQuery<>(
					filter.getOwner().orElse(String.valueOf(query.getOwner())), 
					(filter instanceof NamedFilter) ? ((NamedFilter<?,?>) filter).getName() : "",
					query.getField(), 
					query.getQuery(), 
					query.maybeGetJoins())), query.getInitiatingFilterName());
			}
		};
	}
}
