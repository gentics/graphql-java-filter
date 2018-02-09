package filter;

import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLTypeReference;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class CommonFilters {

    public static <T> List<Filter<T, ?>> createFor(Filter<T, ?> filter) {
        return Arrays.asList(
            orFilter(filter),
            andFilter(filter),
            notFilter(filter)
        );
    }

    private static <T, Q> Filter<T, List<Q>> orFilter(Filter<T, Q> filter) {
        return new Filter<T, List<Q>>() {
            @Override
            public String getName() {
                return "or";
            }

            @Override
            public String getDescription() {
                return "Combines multiple " + filter.getName() + " with logical OR";
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
                return GraphQLList.list(GraphQLTypeReference.typeRef(filter.getName()));
            }
        };
    }

    private static <T, Q> Filter<T, List<Q>> andFilter(Filter<T, Q> filter) {
        return new Filter<T, List<Q>>() {
            @Override
            public String getName() {
                return "and";
            }

            @Override
            public String getDescription() {
                return "Combines multiple " + filter.getName() + " with logical AND";
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
                return GraphQLList.list(GraphQLTypeReference.typeRef(filter.getName()));
            }
        };
    }

    private static <T, Q> Filter<T, Q> notFilter(Filter<T, Q> filter) {
        return new Filter<T, Q>() {
            @Override
            public String getName() {
                return "not";
            }

            @Override
            public String getDescription() {
                return "Negates a " + filter.getName();
            }

            @Override
            public Predicate<T> createPredicate(Q query) {
                return filter.createPredicate(query).negate();
            }

            @Override
            public GraphQLInputType getType() {
                return GraphQLTypeReference.typeRef(filter.getName());
            }
        };
    }
}
