package filter;

import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLList;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLInputObjectType.newInputObject;

public class StringFilter implements Filter<String, Map<String, ?>> {

    private static StringFilter instance;
    public static StringFilter filter() {
        if (instance == null) {
            instance = new StringFilter();
        }
        return instance;
    }
    private final Map<String, Filter<String, ?>> filters;

    private final GraphQLInputType type;

    private StringFilter() {
        List<Filter<String, ?>> filterList = Arrays.asList(
            Filter.create("eq", "Compares two strings for equality", GraphQLString, query -> query::equals),
            Filter.<String, List<String>>create("oneOf", "Checks if the string is equal to one of the given strings", GraphQLList.list(GraphQLString), query -> query::contains),
            Filter.<String, String>create("regex", "Checks if the string matches the given regular expression.", GraphQLString, query -> Pattern.compile(query).asPredicate())
        );

        type = newInputObject()
            .name(getName())
            .description(getDescription())
            .fields(filterList.stream().map(Filter::toObjectField).collect(Collectors.toList()))
            .build();

        filters = filterList.stream().collect(Collectors.toMap(Filter::getName, Function.identity()));
    }

    @Override
    public String getName() {
        return "StringFilter";
    }

    @Override
    public String getDescription() {
        return "Filters strings";
    }

    @Override
    public Predicate<String> createPredicate(Map<String, ?> query) {
        return query.entrySet().stream()
            .map(entry -> {
                Filter filter = filters.get(entry.getKey());
                return filter.createPredicate(entry.getValue());
            })
            .reduce(Predicate::and)
            .orElse(ignore -> true);
    }

    @Override
    public GraphQLInputType getType() {
        return type;
    }
}
