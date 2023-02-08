package com.gentics.graphqlfilter.filter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import com.gentics.graphqlfilter.filter.sql.SqlField;
import com.gentics.graphqlfilter.filter.sql.SqlPredicate;
import com.gentics.graphqlfilter.filter.sql2.FilterQuery;
import com.gentics.graphqlfilter.filter.sql2.FilterOperation;

import graphql.schema.GraphQLInputType;

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
		Objects.requireNonNull(name);
		Objects.requireNonNull(description);
		Objects.requireNonNull(javaMapper);
		this.delegate = delegate;
		this.javaMapper = javaMapper;
		this.name = name;
		this.description = description;
		this.owner = owner;
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
	public Optional<SqlPredicate> maybeGetSqlDefinition(Q query, List<SqlField<?>> fields) {
		return delegate.maybeGetSqlDefinition(query, fields);
	}

	@Override
	public Optional<FilterOperation<?>> maybeGetFilterOperation(FilterQuery<?, Q> query) {
		//return getOwner().flatMap(o -> delegate.maybeGetFilterOperation(new FilterQuery<>(o, getName(), query.getQuery())));
		return delegate.maybeGetFilterOperation(new FilterQuery<>(getOwner().orElse(String.valueOf(query.getOwner())), query.getField(), query.getQuery()));
	}

	@Override
	public Optional<String> getOwner() {
		return Optional.ofNullable(delegate.getOwner().orElse(owner));
	}
}
