package com.gentics.graphqlfilter.filter;


import graphql.schema.GraphQLInputType;

import java.util.function.Predicate;

public interface Filter<T, Q> {
    GraphQLInputType getType();

    Predicate<T> createPredicate(Q query);
}
