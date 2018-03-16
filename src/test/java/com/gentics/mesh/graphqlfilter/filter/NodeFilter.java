package com.gentics.mesh.graphqlfilter.filter;

import com.gentics.mesh.graphqlfilter.model.Node;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class NodeFilter extends MainFilter<Node> {

    private static NodeFilter instance;

    public static NodeFilter filter() {
        if (instance == null) {
            instance = new NodeFilter();
        }
        return instance;
    }

    private NodeFilter() {
        super("NodeFilter", "Filters Nodes");
    }

    @Override
    protected List<FilterField<Node, ?>> getFilters() {
        return Arrays.asList(
            new MappedFilter<>("uuid", "Filters by uuid", StringFilter.filter(), Node::getUuid),
            new MappedFilter<>("schema", "Filters by Schema", SchemaFilter.filter(), Node::getSchema),
            new MappedFilter<>("language", "Filters by Language", StringFilter.filter(), Node::getLanguage),
            new MappedFilter<>("name", "Filters by name", StringFilter.filter(), Node::getName),
            new MappedFilter<>("published", "Filters by published state", BooleanFilter.filter(), Node::isPublished),
            new MappedFilter<>("created", "Filters by creation date", DateFilter.filter(), Node::getCreated),
            new MappedFilter<>("price", "Filters by price", NumberFilter.filter(), node -> node.getPrice() == null ? null : new BigDecimal(node.getPrice().toString()))
        );
    }
}
