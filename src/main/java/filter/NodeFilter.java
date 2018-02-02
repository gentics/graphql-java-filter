package filter;

import graphql.schema.GraphQLInputObjectField;
import graphql.schema.GraphQLInputType;
import model.Node;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static graphql.schema.GraphQLInputObjectField.newInputObjectField;
import static graphql.schema.GraphQLInputObjectType.newInputObject;

public class NodeFilter implements Filter<Node> {

    public static final Filter<Node>[] STATIC_NODE_FILTERS = new Filter[] {
        Filter.stringField("schemaName", "Filters by schema name", Node::getSchemaName),
        Filter.stringField("language", "Filters by language", Node::getLanguage)
    };
    private final Map<String, Filter<Node>> filters;

    public NodeFilter() {
        filters = new HashMap<>();
        Stream.of(STATIC_NODE_FILTERS).forEach(filter -> filters.put(filter.getName(), filter));
    }

    @Override
    public GraphQLInputType createType() {
        return newInputObject()
            .name(getName())
            .description(getDescription())
            .fields(toObjectFields())
            .build();
    }

    private List<GraphQLInputObjectField> toObjectFields() {
        return filters.entrySet().stream()
            .map(this::toObjectField)
            .collect(Collectors.toList());
    }

    private GraphQLInputObjectField toObjectField(Map.Entry<String, Filter<Node>> entry) {
        return newInputObjectField()
            .name(entry.getKey())
            .description("FIXME")
            .type(entry.getValue().createType())
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
    public Predicate<Node> createPredicate(Object query) {
        if (query == null) {
            return ignore -> true;
        }
        if (!(query instanceof Map)) {
            throw new InvalidParameterException("Invalid query type");
        }
        Map<String, Object> map = (Map)query;
        return map.entrySet().stream()
            .map(entry -> filters.get(entry.getKey()).createPredicate(entry.getValue()))
            .reduce((p, q) -> p.and(q))
            .orElse(ignore -> true);
    }
}
