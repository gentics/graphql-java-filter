package com.gentics.graphqlfilter;

import com.gentics.graphqlfilter.filter.DateFilter;
import com.gentics.graphqlfilter.filter.NodeFilter;
import com.gentics.graphqlfilter.model.Node;
import com.gentics.graphqlfilter.model.Schemas;
import com.gentics.graphqlfilter.util.QueryFile;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.GraphQLError;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLObjectType;
import org.junit.Before;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static graphql.Scalars.GraphQLBoolean;
import static graphql.Scalars.GraphQLID;
import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLArgument.newArgument;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLSchema.newSchema;
import static org.junit.Assert.assertEquals;

public class AbstractFilterTest {

	private GraphQL graphQL;

	protected final List<Node> testData;

	public AbstractFilterTest() {
		testData = Arrays.asList(
			new Node("e018fa14-39ed-431c-b09d-b27097b48b85", Schemas.FOLDER,
				DateFilter.parseDate("2018-03-01").toEpochMilli(),
				"de", "images", true, 10d),
			new Node("1f9c42ed-506d-481d-b31e-1a9466e31a81", Schemas.CONTENT,
				DateFilter.parseDate("2018-03-02").toEpochMilli(),
				"en", "Tree: Pine", true, 20d),
			new Node("e240763a-089f-4a25-82bd-d94d63fd45da", Schemas.CONTENT,
				DateFilter.parseDate("2018-03-05T00:30:00+02:00").toEpochMilli(),
				"en", "Tree: Oak", false, 30.5),
			new Node("9352efb8-9546-4239-bde5-c85fe9163d8e", Schemas.CONTENT,
				DateFilter.parseDate("2018-03-05T00:30:00+01:00").toEpochMilli(),
				"en", "Fruit: Apple", true, 0d),
			new Node(null, Schemas.CONTENT, null, null, null, null, null));
	}

	@Before
	public void setupGraphQl() {

		GraphQLObjectType nodeType = GraphQLObjectType.newObject()
			.name("node")
			.field(GraphQLFieldDefinition.newFieldDefinition()
				.name("uuid")
				.type(GraphQLID)
				.dataFetcher(x -> x.<Node>getSource().getUuid())
				.build())
			.field(GraphQLFieldDefinition.newFieldDefinition()
				.name("name")
				.type(GraphQLString)
				.dataFetcher(x -> x.<Node>getSource().getName())
				.build())
			.field(GraphQLFieldDefinition.newFieldDefinition()
				.name("published")
				.type(GraphQLBoolean)
				.dataFetcher(x -> x.<Node>getSource().isPublished())
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
					return testData.stream()
						.filter(p)
						.collect(Collectors.toList());
				})
				.build())
			.build();

		this.graphQL = GraphQL.newGraphQL(newSchema().query(root).build()).build();
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
