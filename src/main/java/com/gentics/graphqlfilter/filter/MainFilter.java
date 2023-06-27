package com.gentics.graphqlfilter.filter;

import static graphql.schema.GraphQLInputObjectType.newInputObject;

import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.gentics.graphqlfilter.Sorting;
import com.gentics.graphqlfilter.filter.operation.Combiner;
import com.gentics.graphqlfilter.filter.operation.FilterOperation;
import com.gentics.graphqlfilter.filter.operation.FilterQuery;
import com.gentics.graphqlfilter.filter.operation.UnformalizableQuery;

import graphql.schema.GraphQLInputObjectField;
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
public abstract class MainFilter<T> implements Filter<T, Map<String, ?>>, NamedFilter<T, Map<String, ?>> {

	protected Optional<Map<String, FilterField<T, ?>>> maybeFilters = Optional.empty();
	protected Optional<GraphQLInputType> maybeType = Optional.empty();
	protected Optional<GraphQLInputType> maybeSortingType = Optional.empty();

	private final Optional<String> ownerType;
	private final String name;
	private final String description;
	private final boolean addCommonFilters;

	/**
	 * Creates a new MainFilter.
	 * 
	 * @param name
	 *            the name of the filter (must be unique across all filters used)
	 * @param description
	 *            the description of the filter
	 * @param filters
	 *            a list of filters to be used that are available in this filter
	 * @param ownerType
	 *            an owner name required for the filter formalizing
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
	 * @param ownerType
	 *            an owner name required for the filter formalizing
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
	 * @param ownerType
	 *            an owner name required for the filter formalizing
	 */
	public MainFilter(String name, String description, boolean addCommonFilters, Optional<String> ownerType) {
		this.ownerType = ownerType;
		this.name = name;
		this.description = description;
		this.addCommonFilters = addCommonFilters;
	}

	/**
	 * Creates a new main filter
	 *
	 * @param name
	 *            the name of the filter (must be unique across all filters used)
	 * @param description
	 *            the description of the filter
	 * @param ownerType
	 *            an owner name required for the filter formalizing
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
		return maybeType.orElseGet(() -> {
			GraphQLInputType ret = newInputObject()
					.name(getName())
					.description(description)
					.fields(getBuiltFilters().values().stream().map(FilterField::toObjectField).collect(Collectors.toList()))
					.build();
			maybeType = Optional.of(ret);
			return ret;
		});
	}

	@Override
	public GraphQLInputType getSortingType() {
		return maybeSortingType.orElseGet(() -> {
			List<GraphQLInputObjectField> fields = getBuiltFilters().values().stream()
					.filter(Filter::isSortable)
					.map(FilterField::toSortableObjectField)
					.collect(Collectors.toList());
			GraphQLInputType ret;
			if (fields.size() > 0) {
				ret = newInputObject()
						.name(getSortingName())
						.description(description)
						.fields(fields)
						.build();
			} else {
				ret = Sorting.getSortingEnumType();
			}
			maybeSortingType = Optional.of(ret);
			return ret;
		});
	}

	@Override
	public Predicate<T> createPredicate(Map<String, ?> query) {
		return query.entrySet().stream()
			.map(entry -> (Predicate<T>) findFilter(entry.getKey()).orElseThrow(() -> new InvalidParameterException(String.format("Filter %s not found", entry.getKey()))).createPredicate(entry.getValue()))
			.reduce(Predicate::and)
			.orElse(ignore -> true);
	}

	@Override
	public FilterOperation<?> createFilterOperation(FilterQuery<?, Map<String, ?>> query) throws UnformalizableQuery {
		try {
			List<FilterOperation<?>> operations = query.getQuery().entrySet().stream()
				.map(entry -> findFilter(entry.getKey())
						.map(f -> {
							try {
								return f.createFilterOperation(
										new FilterQuery<>(
												f.getOwner().orElse(String.valueOf(query.getOwner())), 
												getName(),
												f.getOwner().map(unused -> entry.getKey()).orElse(query.getField()), 
												entry.getValue(), 
												query.maybeGetJoins()))
										.maybeSetFilterId(f.maybeGetFilterId());
							} catch (UnformalizableQuery noop) {
								// Stream API and checked exceptions are not befriended, so we wrap the origin here...
								throw new IllegalArgumentException(noop);
							}
						}).orElseThrow(() -> new InvalidParameterException(String.format("Filter Operation '%s' not found", entry.getKey()))))
				.collect(Collectors.toList());
			if (operations.size() > 0) {
				return Combiner.and(operations, query.getInitiatingFilterName());
			} else {
				throw new UnformalizableQuery(query.getField(), "No operational filters available");
			}
		} catch (Throwable e) {
			// ... and unwrap here
			if (e.getCause() instanceof UnformalizableQuery) {
				throw (UnformalizableQuery) e.getCause();
			} else {
				throw e;
			}
		}
	}

	@SuppressWarnings("unchecked")
	public Optional<Filter<T, Object>> findFilter(String key) {
		return Optional.ofNullable((Filter<T, Object>) maybeFilters.get().get(key));
	}

	@Override
	public Optional<String> getOwner() {
		return ownerType;
	}

	public Map<String, FilterField<T, ?>> getBuiltFilters() {
		return maybeFilters.orElseGet(() -> {
			List<FilterField<T, ?>> commonFilters = addCommonFilters ? CommonFilters.createFor(this, GraphQLTypeReference.typeRef(getName())) : Collections.emptyList();
			List<FilterField<T, ?>> newFilters = getFilters();

			Map<String, FilterField<T, ?>> ret = Stream.concat(
				commonFilters.stream(),
				newFilters.stream()).collect(Collectors.toMap(FilterField::getName, Function.identity()));
			
			maybeFilters = Optional.of(ret);
			return ret;
		});
	}

	public Optional<String> getMaybeOwnerType() {
		return ownerType;
	}

	@Override
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public boolean isAddCommonFilters() {
		return addCommonFilters;
	}
}
