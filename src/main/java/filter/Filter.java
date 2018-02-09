package filter;


import graphql.schema.GraphQLInputObjectField;
import graphql.schema.GraphQLInputObjectType;
import graphql.schema.GraphQLInputType;

import java.util.function.Function;
import java.util.function.Predicate;

import static graphql.schema.GraphQLInputObjectField.newInputObjectField;

public interface Filter<T, Q> {
    String getName();

    String getDescription();

    // TODO Make predicate input type parameterizable
    Predicate<T> createPredicate(Q query);

    GraphQLInputType getType();

    default GraphQLInputObjectField toObjectField() {
        return newInputObjectField()
            .name(getName())
            .description(getDescription())
            .type(getType())
            .build();
    }

    static <T, Q> Filter<T, Q> create(String name, String description, GraphQLInputType type, Function<Q, Predicate<T>> createPredicate) {
        return new Filter<T, Q>() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getDescription() {
                return description;
            }

            @Override
            public Predicate<T> createPredicate(Q query) {
                return createPredicate.apply(query);
            }

            @Override
            public GraphQLInputType getType() {
                return type;
            }
        };
    }
}
