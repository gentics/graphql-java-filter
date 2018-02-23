package com.gentics.mesh.graphqlfilter.filter;

import graphql.schema.GraphQLList;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static graphql.Scalars.GraphQLString;

public class StringFilter extends MainFilter<String> {

    private static StringFilter instance;

    public static StringFilter filter() {
        if (instance == null) {
            instance = new StringFilter();
        }
        return instance;
    }

    private StringFilter() {
        super("StringFilter", "Filters Strings", Arrays.asList(
            FilterField.create("eq", "Compares two strings for equality", GraphQLString, query -> query::equals),
            FilterField.<String, List<String>>create("oneOf", "Checks if the string is equal to one of the given strings", GraphQLList.list(GraphQLString), query -> query::contains),
            FilterField.<String, String>create("regex", "Checks if the string matches the given regular expression.", GraphQLString, query -> Pattern.compile(query).asPredicate())
        ));
    }
}
