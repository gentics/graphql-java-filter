//package filter;
//
//import model.Node;
//
//import java.util.Arrays;
//import java.util.List;
//
//public interface Filters {
//    List<Filter<?>> NODE_FILTERS = Arrays.asList(
//        StringFilter.stringField("schemaName", "Filters by schema name", Node::getSchemaName),
//        StringFilter.stringField("language", "Filters by language", Node::getLanguage)
//    );
//    Filter<Node> NODE_FILTER = new MainFilter<Node>("NodeFilter", "Filters nodes", NODE_FILTERS);
//}
