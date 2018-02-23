package com.gentics.mesh.graphqlfilter.filter;

import com.gentics.mesh.graphqlfilter.model.Node;

import java.util.Arrays;

public class NodeFilter extends MainFilter<Node> {

    private static NodeFilter instance;

    public static NodeFilter filter() {
        if (instance == null) {
            instance = new NodeFilter();
        }
        return instance;
    }

    private NodeFilter() {
        super("NodeFilter", "Filters Nodes", Arrays.asList(
            new MappedFilter<>("uuid", "Filters by uuid", StringFilter.filter(), Node::getUuid),
            new MappedFilter<>("schema", "Filters by Schema", SchemaFilter.filter(), Node::getSchema),
            new MappedFilter<>("language", "Filters by Language", StringFilter.filter(), Node::getLanguage)
        ));
    }
}
