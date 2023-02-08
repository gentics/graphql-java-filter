package com.gentics.graphqlfilter.filter;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import com.gentics.graphqlfilter.filter.operation.FilterOperation;
import com.gentics.graphqlfilter.filter.operation.FilterQuery;
import com.gentics.graphqlfilter.filter.operation.NoOperationException;

import graphql.schema.GraphQLInputType;
import graphql.util.Pair;

/**
 * A filter that maps the type of the predicate input.
 *
 * @param <I>
 *            The original predicate input type
 * @param <T>
 *            The mapped predicate input type
 * @param <Q>
 *            The Java representation of the GraphQLInputType of this query.
 */
public class MappedFilter<I, T, Q> implements FilterField<I, Q> {
	private final Filter<T, Q> delegate;
	private final Function<I, T> javaMapper;
	private final String name;
	private final String description;
	private final String owner;
	private final Optional<Pair<String, String>> maybeJoin;

	/**
	 * Create a MappedFilter.
	 *
	 * @param name
	 *            name of the filter
	 * @param description
	 *            description of the filter
	 * @param delegate
	 *            the original filter to be mapped
	 * @param javaMapper
	 *            A function that maps the predicate input type to another type
	 */
	public MappedFilter(String owner, String name, String description, Filter<T, Q> delegate, Function<I, T> javaMapper) {
		this(owner, name, description, delegate, javaMapper, Optional.empty());
	}

	/**
	 * Create a MappedFilter.
	 *
	 * @param name
	 *            name of the filter
	 * @param description
	 *            description of the filter
	 * @param delegate
	 *            the original filter to be mapped
	 * @param javaMapper
	 *            A function that maps the predicate input type to another type
	 */
	public MappedFilter(String owner, String name, String description, Filter<T, Q> delegate, Function<I, T> javaMapper, Pair<String, String> join) {
		this(owner, name, description, delegate, javaMapper, Optional.ofNullable(join));
	}

	/**
	 * Create a MappedFilter.
	 *
	 * @param name
	 *            name of the filter
	 * @param description
	 *            description of the filter
	 * @param delegate
	 *            the original filter to be mapped
	 * @param javaMapper
	 *            A function that maps the predicate input type to another type
	 */
	public MappedFilter(String owner, String name, String description, Filter<T, Q> delegate, Function<I, T> javaMapper, Optional<Pair<String, String>> maybeJoin) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(description);
		Objects.requireNonNull(javaMapper);
		this.delegate = delegate;
		this.javaMapper = javaMapper;
		this.name = name;
		this.description = description;
		this.owner = owner;
		this.maybeJoin = maybeJoin;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public Predicate<I> createPredicate(Q query) {
		return input -> delegate.createPredicate(query).test(javaMapper.apply(input));
	}

	@Override
	public GraphQLInputType getType() {
		return delegate.getType();
	}

	@Override
	public FilterOperation<?> createFilterOperation(FilterQuery<?, Q> query) throws NoOperationException {
		return delegate.createFilterOperation(new FilterQuery<>(getOwner().orElse(String.valueOf(query.getOwner())), query.getField(), query.getQuery()));
	}

	@Override
	public Optional<String> getOwner() {
		return Optional.ofNullable(delegate.getOwner().orElse(owner));
	}

	public Optional<Pair<String, String>> getMaybeJoin() {
		return maybeJoin;
	}
}
