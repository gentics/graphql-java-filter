package com.gentics.mesh.graphqlfilter.filter;

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

public abstract class MainFilter<T> implements Filter<T, Map<String, Object>> {

    private final Map<String, FilterField<T, ?>> filters;
    private final GraphQLInputType type;

    public MainFilter(String name, String description, List<FilterField<T, ?>> filters) {
        List<FilterField<T, ?>> commonFilters = CommonFilters.createFor(this, GraphQLTypeReference.typeRef(name));

        this.filters = Stream.concat(
            commonFilters.stream(),
            filters.stream()
        ).collect(Collectors.toMap(FilterField::getName, Function.identity()));

        type = newInputObject()
            .name(name)
            .description(description)
            .fields(this.filters.values().stream().map(FilterField::toObjectField).collect(Collectors.toList()))
            .build();
    }

    @Override
    public GraphQLInputType getType() {
        return type;
    }

    @Override
    public Predicate<T> createPredicate(Map<String, Object> query) {
        return query.entrySet().stream()
            .map(entry -> {
                Filter filter = filters.get(entry.getKey());
                if (filter == null) {
                    throw new InvalidParameterException(String.format("Filter %s not found", entry.getKey()));
                }
                return (Predicate<T>) filter.createPredicate(entry.getValue());
            })
            .reduce(Predicate::and)
            .orElse(ignore -> true);
    }
}
