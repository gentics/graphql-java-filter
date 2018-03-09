package com.gentics.mesh.graphqlfilter.filter;

import com.gentics.mesh.graphqlfilter.util.Lazy;
import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLTypeReference;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static graphql.schema.GraphQLInputObjectType.newInputObject;

/**
 * A nested filter that provides various fields to refine the predicate.
 *
 * This filter always has the {@link CommonFilters} included.
 *
 * @param <T> The predicate input type
 */
public abstract class MainFilter<T> implements Filter<T, Map<String, ?>> {

    // We need lazy evaluation here because we need to set the filters and type in the constructor,
    // without having to fear infinite recursion when reusing a filter in itself.
    private final Lazy<Map<String, FilterField<T, ?>>> filters;
    private final Lazy<GraphQLInputType> type;

    /**
     * Creates a new main filter
     *
     * @param name the name of the filter (must be unique across all filters used)
     * @param description the description of the filter
     */
    public MainFilter(String name, String description) {
        this.filters = new Lazy<>(() -> {
            List<FilterField<T, ?>> commonFilters = CommonFilters.createFor(this, GraphQLTypeReference.typeRef(name));
            List<FilterField<T, ?>> filters = getFilters();

            return Stream.concat(
                commonFilters.stream(),
                filters.stream()
            ).collect(Collectors.toMap(FilterField::getName, Function.identity()));
        });

        type = new Lazy<>(() -> newInputObject()
            .name(name)
            .description(description)
            .fields(this.filters.get().values().stream().map(FilterField::toObjectField).collect(Collectors.toList()))
            .build()
        );
    }

    /**
     * Gets a list of filters to be used available in this filter
     */
    protected abstract List<FilterField<T, ?>> getFilters();

    @Override
    public GraphQLInputType getType() {
        return type.get();
    }

    @Override
    public Predicate<T> createPredicate(Map<String, ?> query) {
        return query.entrySet().stream()
            .map(entry -> {
                Filter filter = filters.get().get(entry.getKey());
                if (filter == null) {
                    throw new InvalidParameterException(String.format("Filter %s not found", entry.getKey()));
                }
                return (Predicate<T>) filter.createPredicate(entry.getValue());
            })
            .reduce(Predicate::and)
            //
            .orElse(ignore -> true);
    }
}
