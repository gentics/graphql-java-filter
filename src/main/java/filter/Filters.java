package filter;

import model.Node;

import java.util.Arrays;

public interface Filters {
    Filter<Node> NODE_FILTER = new MainFilter<>("NodeFilter", "Filters nodes", Arrays.asList(
        Filter.stringField("schemaName", "Filters by schema name", Node::getSchemaName),
        Filter.stringField("language", "Filters by language", Node::getLanguage)
    ));
}
