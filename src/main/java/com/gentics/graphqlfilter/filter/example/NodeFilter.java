package com.gentics.graphqlfilter.filter.example;

import com.gentics.graphqlfilter.filter.MainFilter;
import com.gentics.graphqlfilter.filter.MappedFilter;
import com.gentics.graphqlfilter.filter.StringFilter;
import com.gentics.graphqlfilter.model.Node;

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
            new MappedFilter<>("schemaName", "Filters by Schema name", StringFilter.filter(), Node::getSchemaName),
            new MappedFilter<>("language", "Filters by language", StringFilter.filter(), Node::getLanguage)
        ));
    }
}
