package com.gentics.graphqlfilter.filter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import com.gentics.graphqlfilter.filter.operation.FilterOperation;
import com.gentics.graphqlfilter.filter.operation.FilterQuery;
import com.gentics.graphqlfilter.filter.operation.Join;
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
	protected final Filter<T, Q> delegate;
	protected final Function<I, T> javaMapper;
	protected final String name;
	protected final String description;
	protected final String owner;
	protected final Optional<Pair<String, JoinPart>> maybeJoin;
	protected final Optional<String> maybeId;

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

	public MappedFilter(String owner, String name, String description, Filter<T, Q> delegate, Function<I, T> javaMapper, Optional<String> maybeId) {
		this(owner, name, description, delegate, javaMapper, Optional.empty(), maybeId);
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
		this(owner, name, description, delegate, javaMapper, join, Optional.empty());
	}

	public MappedFilter(String owner, String name, String description, Filter<T, Q> delegate, Function<I, T> javaMapper, Pair<String, JoinPart> join, Optional<String> maybeId) {
		this(owner, name, description, delegate, javaMapper, Optional.ofNullable(join), maybeId);
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
	 * @param maybeJoin
	 *            A possible pair of owner field / delegate field, formalizing the join relation between an owner and a delegate.
	 */
	public MappedFilter(String owner, String name, String description, Filter<T, Q> delegate, Function<I, T> javaMapper, Optional<Pair<String, JoinPart>> maybeJoin, Optional<String> maybeId) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(description);
		Objects.requireNonNull(javaMapper);
		this.delegate = delegate;
		this.javaMapper = javaMapper;
		this.name = name;
		this.description = description;
		this.owner = owner;
		this.maybeJoin = maybeJoin;
		this.maybeId = maybeId;
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
		Set<Join> joins = new HashSet<>(getJoins());
		query.maybeGetJoins().ifPresent(join -> joins.addAll(join));
		return delegate.createFilterOperation(
				new FilterQuery<>(
						getOwner().orElse(String.valueOf(query.getOwner())), 
						getName(),
						query.getField(), 
						query.getQuery(), 
						Optional.of(joins))).maybeSetFilterId(maybeGetFilterId());
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
	public Set<Join> getJoins() {
		return maybeJoin
				.map(join -> new Join(new JoinPart(owner, join.first), new JoinPart(delegate.getOwner().orElse(join.second.getTable()), join.second.getField())))
				.map(join -> Collections.singleton(join))
				.orElse(new HashSet<>());
	}

	@Override
	public boolean isSortable() {
		return delegate.isSortable();
	}

	@Override
	public GraphQLInputType getSortingType() {
		return delegate.getSortingType();
	}

	@Override
	public Optional<String> maybeGetFilterId() {
		return maybeId;
	}
}
