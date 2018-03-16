package com.gentics.mesh.graphqlfilter.filter;

import graphql.Scalars;
import graphql.schema.GraphQLInputType;

import java.util.function.Predicate;

import static com.gentics.mesh.graphqlfilter.util.FilterUtil.nullablePredicate;

public class BooleanFilter implements Filter<Boolean, Boolean> {

    private static BooleanFilter instance;

    /**
     * Get the singleton boolean filter
     */
    public static BooleanFilter filter() {
        if (instance == null) {
            instance = new BooleanFilter();
        }
        return instance;
    }

    @Override
    public GraphQLInputType getType() {
        return Scalars.GraphQLBoolean;
    }

    @Override
    public Predicate<Boolean> createPredicate(Boolean query) {
        return query::equals;
    }
}
