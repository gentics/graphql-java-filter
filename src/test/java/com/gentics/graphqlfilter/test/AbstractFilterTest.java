package com.gentics.graphqlfilter.test;

import com.gentics.graphqlfilter.test.filter.NodeFilter;
import com.gentics.graphqlfilter.test.model.Schemas;
import com.gentics.graphqlfilter.test.util.QueryFile;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.GraphQLError;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLObjectType;
import com.gentics.graphqlfilter.test.model.Node;
import org.junit.Before;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static graphql.Scalars.GraphQLID;
import static graphql.schema.GraphQLArgument.newArgument;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLSchema.newSchema;
import static org.junit.Assert.assertEquals;

public class AbstractFilterTest {

    private GraphQL graphQL;

    @Before
    public void setupGraphQl() {

        GraphQLObjectType nodeType = GraphQLObjectType.newObject()
            .name("node")
            .field(GraphQLFieldDefinition.newFieldDefinition()
                .name("uuid")
                .type(GraphQLID)
                .dataFetcher(x -> x.<Node>getSource().getUuid())
                .build())
            .build();

        GraphQLObjectType root = GraphQLObjectType.newObject()
            .name("root")
            .field(newFieldDefinition()
                .name("nodes")
                .argument(newArgument().name("filter").type(NodeFilter.filter().getType()).build())
                .type(GraphQLList.list(nodeType))
                .dataFetcher(x -> {
                    Predicate<Node> p = NodeFilter.filter().createPredicate(x.getArgument("filter"));
                    return testData().stream()
                        .filter(p)
                        .collect(Collectors.toList());
                })
                .build())
            .build();

        this.graphQL = GraphQL.newGraphQL(newSchema().query(root).build()).build();
    }

    private List<Node> testData() {

        return Arrays.asList(
            new Node("e018fa14-39ed-431c-b09d-b27097b48b85", Schemas.FOLDER, Instant.ofEpochSecond(1517583296), "de"),
            new Node("1f9c42ed-506d-481d-b31e-1a9466e31a81", Schemas.CONTENT, Instant.ofEpochSecond(1417583296), "en")
        );
    }

    protected ExecutionResult queryNodes(String query) {
        return queryNodes(query, true);
    }

    protected ExecutionResult queryNodes(String query, boolean assertSuccess) {
        ExecutionResult result = graphQL.execute(query);
        if (assertSuccess) {
            List<GraphQLError> errors = result.getErrors();
            assertEquals(errors.toString(), result.getErrors().size(), 0);
        }
        return result;
    }


    protected ExecutionResult queryNodes(QueryFile query) {
        return queryNodes(query, true);
    }

    protected ExecutionResult queryNodes(QueryFile query, boolean assertSuccess) {
        return queryNodes(query.getQuery(), assertSuccess);
    }

    protected List<Map<String, ?>> queryNodesAsList(QueryFile query) {
        Map<String, List<Map<String, ?>>> result = queryNodes(query).getData();
        return result.get("nodes");
    }
}
