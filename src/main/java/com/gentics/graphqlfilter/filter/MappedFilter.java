package com.gentics.graphqlfilter.filter;

import graphql.schema.GraphQLInputType;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

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
	private final Function<I, T> mapper;
	private final String name;
	private final String description;

	/**
	 * Create a MappedFilter.
	 *
	 * @param name
	 *            name of the filter
	 * @param description
	 *            description of the filter
	 * @param delegate
	 *            the original filter to be mapped
	 * @param mapper
	 *            A function that maps the predicate input type to another type
	 */
	public MappedFilter(String name, String description, Filter<T, Q> delegate, Function<I, T> mapper) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(description);
		Objects.requireNonNull(mapper);
		this.delegate = delegate;
		this.mapper = mapper;
		this.name = name;
		this.description = description;
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
		return input -> delegate.createPredicate(query).test(mapper.apply(input));
	}

	@Override
	public GraphQLInputType getType() {
		return delegate.getType();
	}
}
