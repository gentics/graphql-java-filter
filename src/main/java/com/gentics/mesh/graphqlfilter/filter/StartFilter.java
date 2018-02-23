package com.gentics.mesh.graphqlfilter.filter;

import graphql.schema.GraphQLArgument;

public interface StartFilter<T, Q> extends Filter<T, Q> {
    default GraphQLArgument createFilterArgument() {
        return GraphQLArgument.newArgument()
            .name("filter")
            .description("Filters the list of elements")
            .type(getType())
            .build();
    }
}
