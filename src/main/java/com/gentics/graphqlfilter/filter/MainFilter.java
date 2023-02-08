package com.gentics.graphqlfilter.filter;

import static graphql.schema.GraphQLInputObjectType.newInputObject;

import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.gentics.graphqlfilter.filter.operation.Combiner;
import com.gentics.graphqlfilter.filter.operation.FilterOperation;
import com.gentics.graphqlfilter.filter.operation.FilterQuery;
import com.gentics.graphqlfilter.filter.operation.NoOperationException;
import com.gentics.graphqlfilter.util.Lazy;

import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLTypeReference;

/**
 * A nested filter that provides various fields to refine the predicate.
 *
 * This filter always has the {@link CommonFilters} included.
 *
 * @param <T>
 *            The predicate input type
 */
public abstract class MainFilter<T> implements Filter<T, Map<String, ?>> {

	// We need lazy evaluation here because we need to set the filters and type in the constructor,
	// without having to fear infinite recursion when reusing a filter in itself.
	private final Lazy<Map<String, FilterField<T, ?>>> filters;
	private final Lazy<GraphQLInputType> type;
	private final Optional<String> ownerType;

	/**
	 * Creates a new MainFilter.
	 * 
	 * @param name
	 *            the name of the filter (must be unique across all filters used)
	 * @param description
	 *            the description of the filter
	 * @param filters
	 *            a list of filters to be used that are available in this filter
	 */
	public static <T> MainFilter<T> mainFilter(String name, String description, List<FilterField<T, ?>> filters, Optional<String> ownerType) {
		return mainFilter(name, description, filters, true, ownerType);
	}

	/**
	 * Creates a new MainFilter.
	 * 
	 * @param name
	 *            the name of the filter (must be unique across all filters used)
	 * @param description
	 *            the description of the filter
	 * @param filters
	 *            a list of filters to be used that are available in this filter
	 * @param addCommonFilters
	 *            set to false to prevent adding of common composition types
	 */
	public static <T> MainFilter<T> mainFilter(String name, String description, List<FilterField<T, ?>> filters, boolean addCommonFilters, Optional<String> ownerType) {
		return new MainFilter<T>(name, description, addCommonFilters, ownerType) {
			@Override
			protected List<FilterField<T, ?>> getFilters() {
				return filters;
			}
		};
	}

	/**
	 * Creates a new main filter
	 *
	 * @param name
	 *            the name of the filter (must be unique across all filters used)
	 * @param description
	 *            the description of the filter
	 * @param addCommonFilters
	 *            set to false to prevent adding of common composition types
	 */
	public MainFilter(String name, String description, boolean addCommonFilters, Optional<String> ownerType) {
		this.ownerType = ownerType;
		this.filters = new Lazy<>(() -> {
			List<FilterField<T, ?>> commonFilters = addCommonFilters ? CommonFilters.createFor(this, GraphQLTypeReference.typeRef(name))
				: Collections.emptyList();
			List<FilterField<T, ?>> filters = getFilters();

			return Stream.concat(
				commonFilters.stream(),
				filters.stream()).collect(Collectors.toMap(FilterField::getName, Function.identity()));
		});
		type = new Lazy<>(() -> newInputObject()
			.name(name)
			.description(description)
			.fields(this.filters.get().values().stream().map(FilterField::toObjectField).collect(Collectors.toList()))
			.build());
	}

	/**
	 * Creates a new main filter
	 *
	 * @param name
	 *            the name of the filter (must be unique across all filters used)
	 * @param description
	 *            the description of the filter
	 */
	public MainFilter(String name, String description, Optional<String> ownerType) {
		this(name, description, true, ownerType);
	}

	/**
	 * Gets a list of filters to be used that are available in this filter
	 */
	protected abstract List<FilterField<T, ?>> getFilters();

	@Override
	public GraphQLInputType getType() {
		return type.get();
	}

	@Override
	public Predicate<T> createPredicate(Map<String, ?> query) {
		return query.entrySet().stream()
			.map(entry -> (Predicate<T>) findFilter(entry.getKey()).orElseThrow(() -> new InvalidParameterException(String.format("Filter %s not found", entry.getKey()))).createPredicate(entry.getValue()))
			.reduce(Predicate::and)
			.orElse(ignore -> true);
	}

	@Override
	public FilterOperation<?> createFilterOperation(FilterQuery<?, Map<String, ?>> query) throws NoOperationException {
		try {
			List<FilterOperation<?>> operations = query.getQuery().entrySet().stream()
				.map(entry -> findFilter(entry.getKey())
						.map(f -> {
							try {
								return f.createFilterOperation(new FilterQuery<>(f.getOwner().orElse(String.valueOf(query.getOwner())), f.getOwner().map(unused -> entry.getKey()).orElse(query.getField()), entry.getValue()));
							} catch (NoOperationException noop) {
								throw new NoSuchElementException(noop.getLocalizedMessage());
							}
						})
						.orElseThrow(() -> new InvalidParameterException(String.format("Filter Operation %s not found", entry.getKey()))))
				.collect(Collectors.toList());
			if (operations.size() > 0) {
				return Combiner.and(operations);
			} else {
				throw new NoOperationException("No operational filters available");
			}
		} catch (NoSuchElementException e) {
			throw new NoOperationException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public Optional<Filter<T, Object>> findFilter(String key) {
		return Optional.ofNullable((Filter<T, Object>) filters.get().get(key));
	}

	@Override
	public Optional<String> getOwner() {
		return ownerType;
	}
}
