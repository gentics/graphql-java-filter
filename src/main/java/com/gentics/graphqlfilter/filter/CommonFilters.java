package com.gentics.graphqlfilter.filter;

import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLTypeReference;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class CommonFilters {

    public static <T> List<FilterField<T, ?>> createFor(Filter<T, ?> filter, GraphQLTypeReference type) {
        return Arrays.asList(
            orFilter(filter, type),
            andFilter(filter, type),
            notFilter(filter, type)
        );
    }

    private static <T, Q> FilterField<T, List<Q>> orFilter(Filter<T, Q> filter, GraphQLInputType type) {
        return new FilterField<T, List<Q>>() {
            @Override
            public String getName() {
                return "or";
            }

            @Override
            public String getDescription() {
                return "Applies if any filters match.";
            }

            @Override
            public Predicate<T> createPredicate(List<Q> query) {
                return query.stream()
                    .map(filter::createPredicate)
                    .reduce(Predicate::or)
                    .orElse(ignore -> true);
            }

            @Override
            public GraphQLInputType getType() {
                return type;
            }
        };
    }

    private static <T, Q> FilterField<T, List<Q>> andFilter(Filter<T, Q> filter, GraphQLInputType type) {
        return new FilterField<T, List<Q>>() {
            @Override
            public String getName() {
                return "and";
            }

            @Override
            public String getDescription() {
                return "Applies if all filters match.";
            }

            @Override
            public Predicate<T> createPredicate(List<Q> query) {
                return query.stream()
                    .map(filter::createPredicate)
                    .reduce(Predicate::and)
                    .orElse(ignore -> true);
            }

            @Override
            public GraphQLInputType getType() {
                return type;
            }
        };
    }

    private static <T, Q> FilterField<T, Q> notFilter(Filter<T, Q> filter, GraphQLInputType type) {
        return new FilterField<T, Q>() {
            @Override
            public String getName() {
                return "not";
            }

            @Override
            public String getDescription() {
                return "Negates a filter.";
            }

            @Override
            public Predicate<T> createPredicate(Q query) {
                return filter.createPredicate(query).negate();
            }

            @Override
            public GraphQLInputType getType() {
                return type;
            }
        };
    }
}
