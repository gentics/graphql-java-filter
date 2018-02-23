package com.gentics.graphqlfilter.test;

import com.gentics.graphqlfilter.test.util.QueryFile;
import graphql.ExecutionResult;
import graphql.GraphQLError;
import graphql.validation.ValidationError;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class NestedFilterTest extends AbstractFilterTest {
    @Test
    public void testEnum() {
        List<Map<String, ?>> nodes = queryNodesAsList(new QueryFile("nested", "enum"));
        assertEquals(1, nodes.size());
        assertEquals("2", nodes.get(0).get("id"));
    }

    @Test
    public void testInvalidEnum() {
        ExecutionResult result = queryNodes(new QueryFile("nested", "invalidEnum"), false);
        assertEquals(1, result.getErrors().size());
        GraphQLError error = result.getErrors().get(0);
        ValidationError validationError = (ValidationError) error;
        assertEquals("WrongType", validationError.getValidationErrorType().name());
    }

    @Test
    public void testSchemaUuid() {
        List<Map<String, ?>> nodes = queryNodesAsList(new QueryFile("nested", "schemaUuid"));
        assertEquals(1, nodes.size());
        assertEquals("2", nodes.get(0).get("id"));
    }
}
