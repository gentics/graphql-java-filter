package com.gentics.graphqlfilter.filter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import com.gentics.graphqlfilter.filter.operation.FilterOperation;
import com.gentics.graphqlfilter.filter.operation.FilterQuery;
import com.gentics.graphqlfilter.filter.operation.JoinPart;
import com.gentics.graphqlfilter.filter.operation.UnformalizableQuery;

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
	private final Optional<Pair<String, JoinPart>> maybeJoin;

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
	 *            A function that maps the predicate input type to another type using Java API
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
	 * @param join
	 *            A pair of owner field / delegate field, formalizing the join relation between an owner and a delegate.
	 */
	public MappedFilter(String owner, String name, String description, Filter<T, Q> delegate, Function<I, T> javaMapper, Pair<String, JoinPart> join) {
		this(owner, name, description, delegate, javaMapper, Optional.ofNullable(join));
	}

	/**
	 * Create a MappedFilter.
	 * 
	 * @param owner
	 *            A mandatory owner name of this mapped filter. See {@link Filter#getOwner()} for more info.
	 * @param name
	 *            name of the filter
	 * @param description
	 *            description of the filter
	 * @param delegate
	 *            the original filter to be mapped
	 * @param javaMapper
	 *            A function that maps the predicate input type to another type
	 * @param join
	 *            A possible pair of owner field / delegate field, formalizing the join relation between an owner and a delegate.
	 */
	public MappedFilter(String owner, String name, String description, Filter<T, Q> delegate, Function<I, T> javaMapper, Optional<Pair<String, JoinPart>> maybeJoin) {
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
	public FilterOperation<?> createFilterOperation(FilterQuery<?, Q> query) throws UnformalizableQuery {
		Map<JoinPart, JoinPart> joins = new HashMap<>(getJoins());
		query.getMaybeJoins().ifPresent(join -> joins.putAll(join));
		return delegate.createFilterOperation(
				new FilterQuery<>(
						getOwner().orElse(String.valueOf(query.getOwner())), 
						query.getField(), 
						query.getQuery(), 
						Optional.of(joins)));
	}

	@Override
	public Optional<String> getOwner() {
		return Optional.ofNullable(delegate.getOwner().orElse(owner));
	}

	/**
	 * Get back the possible owner / delegate relation info.
	 * 
	 * @return
	 */
	public Map<JoinPart, JoinPart> getJoins() {
		return maybeJoin
				.map(join -> Pair.pair(new JoinPart(owner, join.first), new JoinPart(delegate.getOwner().orElse(join.second.getTable()), join.second.getField())))
				.map(join -> Collections.singletonMap(join.first, join.second))
				.orElse(new HashMap<>());
	}

	@Override
	public boolean isSortable() {
		return delegate.isSortable();
	}

	@Override
	public GraphQLInputType getSortingType() {
		return delegate.getSortingType();
	}
}
