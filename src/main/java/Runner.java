import filter.Filters;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.GraphQLError;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLObjectType;
import model.Node;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLArgument.newArgument;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLSchema.newSchema;

public class Runner {

    public static void main(String[] args) {
        Stream<Node> nodes = Stream.of(
            new Node("folder", Instant.ofEpochSecond(1517583296), "de"),
            new Node("vehicle", Instant.ofEpochSecond(1417583296), "en")
        );

        GraphQLObjectType root = GraphQLObjectType.newObject()
            .name("root")
            .field(newFieldDefinition()
                .name("nodes")
                .argument(newArgument().name("filter").type(Filters.NODE_FILTER.getType()).build())
                .type(GraphQLList.list(GraphQLString))
                .dataFetcher(x -> {
                    Predicate<Node> p = Filters.NODE_FILTER.createPredicate(x.getArgument("filter"));
                    return nodes
                        .filter(p)
                        .collect(Collectors.toList());
                })
                .build())
            .build();

        GraphQL build = GraphQL.newGraphQL(newSchema().query(root).build()).build();
        String query = getQuery();
        ExecutionResult executionResult = build.execute(query);

        List<GraphQLError> errors = executionResult.getErrors();
        if (executionResult.getErrors().size() > 0) {
            errors.stream()
                //.map(GraphQLError::getMessage)
                .forEach(System.err::println);
        } else {
            System.out.println(executionResult.getData().toString());
        }
    }

    private static String getQuery() {
        try {
            return new String(Files.readAllBytes(Paths.get(Runner.class.getResource("query.gql").toURI())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
