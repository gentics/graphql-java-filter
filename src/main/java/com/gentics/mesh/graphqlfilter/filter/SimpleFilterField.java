package com.gentics.mesh.graphqlfilter.filter;

import graphql.schema.GraphQLInputType;

import java.util.Objects;
import java.util.function.Predicate;

public class SimpleFilterField<T, Q> implements FilterField<T, Q> {
    private final String name;
    private final String description;
    private final Filter<T, Q> delegate;

    public SimpleFilterField(String name, String description, Filter<T, Q> delegate) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(description);
        Objects.requireNonNull(delegate);
        this.name = name;
        this.description = description;
        this.delegate = delegate;
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
    public GraphQLInputType getType() {
        return delegate.getType();
    }

    @Override
    public Predicate<T> createPredicate(Q query) {
        return delegate.createPredicate(query);
    }
}
