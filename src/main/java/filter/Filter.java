package filter;


import graphql.schema.GraphQLInputObjectField;
import graphql.schema.GraphQLInputType;

import java.util.function.Function;
import java.util.function.Predicate;

import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLInputObjectField.newInputObjectField;

public interface Filter<T> {
    String getName();
    String getDescription();
    Predicate<T> createPredicate(Object query);
    GraphQLInputType getType();

    default GraphQLInputObjectField toObjectField() {
        return newInputObjectField()
            .name(getName())
            .description(getDescription())
            .type(getType())
            .build();
    }

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
            public GraphQLInputType getType() {
                return GraphQLString;
            }
        };
    }
}
