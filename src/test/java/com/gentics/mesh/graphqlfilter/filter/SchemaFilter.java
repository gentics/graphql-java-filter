package com.gentics.mesh.graphqlfilter.filter;

import com.gentics.mesh.graphqlfilter.model.Schema;
import graphql.schema.GraphQLEnumType;
import graphql.schema.GraphQLInputType;

import java.util.Arrays;

public class SchemaFilter extends MainFilter<Schema> {

    private static SchemaFilter instance;

    public static SchemaFilter filter() {
        if (instance == null) {
            instance = new SchemaFilter();
        }
        return instance;
    }

    private SchemaFilter() {
        super("SchemaFilter", "Filters schemas", Arrays.asList(
            new MappedFilter<>("name", "Filters by name", StringFilter.filter(), Schema::getName),
            new MappedFilter<>("uuid", "Filters by uuid", StringFilter.filter(), Schema::getUuid),
            FilterField.<Schema, String>create("is", "Filters by Schema Type", getSchemaEnum(), name -> schema -> schema.getName().equals(name))
        ));
    }

    private static GraphQLInputType getSchemaEnum() {
        return GraphQLEnumType.newEnum()
            .name("SchemaEnum")
            .value("folder", "folder")
            .value("content", "content")
            .build();
    }
}
