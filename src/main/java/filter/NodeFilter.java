package filter;

import graphql.schema.GraphQLInputType;
import model.Node;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static graphql.schema.GraphQLInputObjectField.newInputObjectField;
import static graphql.schema.GraphQLInputObjectType.newInputObject;

public class NodeFilter implements Filter<Node, Map<String, Object>> {

    private static NodeFilter instance;
    private final GraphQLInputType type;

    public static NodeFilter filter() {
        if (instance == null) {
            instance = new NodeFilter();
        }
        return instance;
    }
    private Map<String, Filter<Node, ?>> commonFilters;

    private NodeFilter() {
        List<Filter<Node, ?>> filters = CommonFilters.createFor(this);
        commonFilters = filters.stream()
            .collect(Collectors.toMap(Filter::getName, Function.identity()));

        type = newInputObject()
            .name("NodeFilter")
            .description("Filters nodes")
            .field(newInputObjectField().name("schemaName").description("Filters by Schema name").type(StringFilter.filter().getType()))
            .field(newInputObjectField().name("language").description("Filters by language").type(StringFilter.filter().getType()))
            .fields(filters.stream().map(Filter::toObjectField).collect(Collectors.toList()))
            .build();
    }

    @Override
    public String getName() {
        return "NodeFilter";
    }

    @Override
    public String getDescription() {
        return "Filters nodes";
    }

    @Override
    public Predicate<Node> createPredicate(Map<String, Object> query) {
        return query.entrySet().stream()
            .<Predicate<Node>>map(entry -> {
                if (entry.getKey().equals("schemaName")) {
                    Predicate pred = StringFilter.filter().createPredicate((Map) entry.getValue());
                    return t -> pred.test(t.getSchemaName());
                } else if (entry.getKey().equals("language")) {
                    Predicate pred = StringFilter.filter().createPredicate((Map) entry.getValue());
                    return t -> pred.test(t.getLanguage());
                } else if (entry.getKey().equals("or")) {
                    Filter<Node, Object> filter = (Filter<Node, Object>) commonFilters.get("or");
                    return filter.createPredicate(entry.getValue());
                } else if (entry.getKey().equals("and")) {
                    Filter<Node, Object> filter = (Filter<Node, Object>) commonFilters.get("and");
                    return filter.createPredicate(entry.getValue());
                } else if (entry.getKey().equals("not")) {
                    Filter<Node, Object> filter = (Filter<Node, Object>) commonFilters.get("not");
                    return filter.createPredicate(entry.getValue());
                } else {
                    throw new InvalidParameterException();
                }
            })
            .reduce(Predicate::and)
            .orElse(ignore -> true);
    }

    @Override
    public GraphQLInputType getType() {
        return type;
    }
}
