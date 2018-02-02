package filter;


import graphql.schema.GraphQLInputType;

import java.util.function.Function;
import java.util.function.Predicate;

import static graphql.Scalars.GraphQLString;

public interface Filter<T> {
    String getName();
    String getDescription();
    Predicate<T> createPredicate(Object query);
    GraphQLInputType createType();

    static <T> Filter<T> stringField(String name, String description, Function<T, String> stringProvider) {
        return new Filter<T>() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getDescription() {
                return description;
            }

            @Override
            public Predicate<T> createPredicate(Object query) {
                return t -> stringProvider.apply(t).equals(query);
            }

            @Override
            public GraphQLInputType createType() {
                return GraphQLString;
            }
        };
    }
}
